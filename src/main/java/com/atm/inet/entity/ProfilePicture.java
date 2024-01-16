package com.atm.inet.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "m_profile_picture")
@SuperBuilder
@IdClass(String.class)
public class ProfilePicture extends BaseFile {

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private UserCredential user;
}
