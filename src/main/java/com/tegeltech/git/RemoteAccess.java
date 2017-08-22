package com.tegeltech.git;

import com.jcraft.jsch.Session;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.transport.*;

public class RemoteAccess {

    private String username;

    private String password;

    public RemoteAccess(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public CredentialsProvider getCredentialsProvider() {
        return new UsernamePasswordCredentialsProvider(username, password);
    }

    public TransportConfigCallback getTransportConfigCallback() {
        final SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
            @Override
            protected void configure(OpenSshConfig.Host hc, Session session) {
                // do nothing
            }
        };
        return transport -> {
            SshTransport sshTransport = (SshTransport) transport;
            sshTransport.setSshSessionFactory(sshSessionFactory);
        };
    }
}
