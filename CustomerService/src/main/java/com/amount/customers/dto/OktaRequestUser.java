package com.amount.customers.dto;

import lombok.*;

import java.util.Arrays;
import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OktaRequestUser {
    private Profile profile;
    private List<String> groupIds;
    private Credentials credentials;
    @Getter
    @Setter
    @Builder
    public static class Profile{
        private String firstName;
        private String lastName;
        private String email;
        private String login;
    }
    @Getter
    @Setter
    @Builder
    public static class Password{
        private String value;
    }
    @Setter
    @Getter
    @Builder
    public static class Credentials{
        private Password password;
    }
}
