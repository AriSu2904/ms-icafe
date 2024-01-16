package com.atm.inet.entity;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "m_user_credential")
public class UserCredential {

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private String id;

    @Column(name = "email", unique = true)
    private String email;

    private String password;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private Boolean isActive;


    @OneToOne(mappedBy = "user")
    @ToString.Exclude
    private ProfilePicture profilePicture;
}
