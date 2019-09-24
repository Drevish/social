package com.drevish.social.model.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

@Data
@Entity
public class UserInfo {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    private User user;

    private String name;
    private String surname;
}
