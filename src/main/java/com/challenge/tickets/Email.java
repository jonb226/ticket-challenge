package com.challenge.tickets;

/**
 * Wrap the some strings in their own type to help ensure parameter correctness
 */
public class Email extends OpaqueId<String> {
    private Email(String id) {
        super(id);
    }

    public static Email from(String email){
        return new Email(email);
    }
}
