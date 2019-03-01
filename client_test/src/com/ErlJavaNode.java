package com;

import com.ericsson.otp.erlang.OtpConnection;
import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangTuple;
import com.ericsson.otp.erlang.OtpPeer;
import com.ericsson.otp.erlang.OtpSelf;

public class ErlJavaNode {

    public static void main(String[] args) throws Exception {

        String cookie = "game";

        OtpSelf self = new OtpSelf("java_node@127.0.0.1", cookie);
        OtpPeer other = new OtpPeer("node10@192.168.1.97");
        OtpConnection connection = self.connect(other);
        System.out.println("java node start");

        if (connection.isConnected()) {
            System.out.println("connect ok");

            OtpErlangObject[] msg = new OtpErlangObject[2];
            msg[0] = self.pid();
            msg[1] = new OtpErlangAtom("hello, world");
            OtpErlangTuple tuple = new OtpErlangTuple(msg);
            connection.send("erl_node_name", tuple);
        } else {
            System.out.println("connect fail");
        }
    }
}
