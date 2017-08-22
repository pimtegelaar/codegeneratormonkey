package com.tegeltech.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RefSpec;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class GitHelper {

    public static void main(String[] args) throws Exception {
        if (args.length < 4) {
            System.out.println("Need username and password in the arguments!");
            return;
        }
        String username = args[0];
        String password = args[1];
        String repositoryLocation = args[2];
        String branchName = args[3];
        GitHelper gitHelper = new GitHelper(new RemoteAccess(username, password));
        Git git = gitHelper.git(repositoryLocation);
        String number = "019";
        String sourceFiles = "src\\main\\java\\com\\tegeltech\\mutationtestingexperiment\\PrimeFinder" + number + ".java";
        String testFiles = "src\\test\\java\\com\\tegeltech\\mutationtestingexperiment\\PrimeFinder" + number + "Test.java";
//        gitHelper.addAll(git, sourceFiles);
//        gitHelper.addAll(git, testFiles);
        gitHelper.commit(git, "updating " + number);
        gitHelper.push(git, branchName);
    }

    private RemoteAccess remoteAccess;

    public GitHelper(RemoteAccess remoteAccess) {
        this.remoteAccess = remoteAccess;
    }


    public void checkout(String repositoryLocation, String branchName) throws IOException, GitAPIException {
        Repository repo = repo(repositoryLocation);
        Git git = new Git(repo);
        git.checkout().setName(branchName).call();
    }

    private Repository repo(String repositoryLocation) throws IOException {
        File gitDir = new File(repositoryLocation);
        return new FileRepositoryBuilder().setGitDir(gitDir).build();
    }


    public Iterable<PushResult> push(Git git, String branchName) throws IOException, URISyntaxException, GitAPIException {
        String remote = "origin";
        String branch = "refs/heads/" + branchName;

        RefSpec spec = new RefSpec(branch + ":" + branch);
        CredentialsProvider credentialsProvider = remoteAccess.getCredentialsProvider();
        System.out.println("Starting push...");
        Iterable<PushResult> pushResults = git.push().setRemote(remote).setCredentialsProvider(credentialsProvider)
                .setRefSpecs(spec).call();
        System.out.println("Push finished");
        return pushResults;
    }

    private Git git(String repositoryLocation) throws IOException {
        System.out.println("Initializing...");
        Repository db = repo(repositoryLocation);
        return new Git(db);
    }

    public void addAll(Git git, String filePattern) throws IOException, GitAPIException {
        System.out.println("Starting git add");
        git.add().setUpdate(true).addFilepattern(filePattern).call();
        System.out.println("Finished add");
    }

    public void commit(Git git, String message) throws GitAPIException {
        System.out.println("Starting commit");
        git.commit().setMessage(message).call();
        System.out.println("Finished commit");
    }

}
