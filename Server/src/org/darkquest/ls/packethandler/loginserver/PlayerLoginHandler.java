package org.darkquest.ls.packethandler.loginserver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.apache.mina.common.IoSession;
import org.darkquest.ls.Server;
import org.darkquest.ls.model.World;
import org.darkquest.ls.net.LSPacket;
import org.darkquest.ls.net.Packet;
import org.darkquest.ls.packetbuilder.loginserver.PlayerLoginPacketBuilder;
import org.darkquest.ls.packethandler.PacketHandler;
import org.darkquest.ls.util.DataConversions;


public class PlayerLoginHandler implements PacketHandler {
    public static ArrayList<String> badClients = new ArrayList<String>();
    private PlayerLoginPacketBuilder builder = new PlayerLoginPacketBuilder();

    public void handlePacket(Packet p, IoSession session) throws Exception {
	final long uID = ((LSPacket) p).getUID();
	World world = (World) session.getAttachment();
	long user = p.readLong();
	String ip = DataConversions.IPToString(p.readLong());
	String pass = p.readString(40).trim();
	String className = p.readString();
	byte loginCode = validatePlayer(user, pass, ip);

	builder.setUID(uID);
	if (loginCode == 0 || loginCode == 1 || loginCode == 99) {
	    try {
		badClients.add(DataConversions.hashToUsername(user));
		System.out.println("Class: " + className + " Player: " + DataConversions.hashToUsername(user));
	    } catch (Exception e) {
		System.out.println("Exception in classname printer :" + e.getMessage());
	    }
	    try {
		Server.db.updateQuery("UPDATE `rsca2_players` SET online=1 WHERE user='" + user + "'");
	    } catch (Exception e) {
	    }

	    builder.setPlayer(Server.getServer().findSave(user, world), loginCode);
	    world.registerPlayer(user, ip);
	} else {
	    builder.setPlayer(null, loginCode);
	}

	LSPacket packet = builder.getPacket();
	if (packet != null) {
	    session.write(packet);
	}
    }

    private byte validatePlayer(long user, String pass, String ip) {
	Server server = Server.getServer();
	byte returnVal = 0;

	try {
		ResultSet result = Server.db.getQuery("SELECT r.pass, r.banned, r.owner, u.group_id, b.id AS b_id FROM `rsca2_players` AS r INNER JOIN `users` AS u ON u.id=r.owner LEFT JOIN `bans` AS b on (b.username LIKE u.username OR b.ip LIKE '" + ip + "') WHERE `user`='" + user + "'");
	    if (!result.next()) {
	    	return 2;
	    }
	    if (!pass.equalsIgnoreCase(result.getString("pass"))) {
	    	return 2;
	    }
	    if (result.getInt("banned") == 1 || result.getInt("b_id") != 0) {
			System.out.println("Banned player: " + DataConversions.hashToUsername(user) + " trying to login.");
			return 6;
	    }

	    if (result.getInt("group_id") == 1 || result.getInt("group_id") == 2) {
	    	returnVal = 99;
	    }

	    int owner = result.getInt("owner");
	    for (World w : server.getWorlds()) {
		for (Entry<Long, Integer> player : w.getPlayers()) {
		    if (player.getKey() == user) {
			return 3;
		    }
		    if (player.getValue() == owner) {
			return 9;
		    }
		}
		if (w.hasPlayer(user)) {
		    return 3;
		}
	    }
	    return returnVal;
	} catch (SQLException e) {
	    System.out.println("Exception in PlayerLoginHandler :" + e.getMessage());
	    // System.out.println(e.getMessage(), e);
	    return 7;
	}
    }
}