package com.salesagents.dataaccess.repository.security;

public interface HashAlgorithm {
    boolean authenticate(String password, String hashedPassword);
    String hash(String password);
}
