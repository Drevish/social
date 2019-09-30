package com.drevish.social.model.entity;


import com.drevish.social.anno.Name;
import com.drevish.social.anno.Surname;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserInfo {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    private User user;

    @Name
    private String name;

    @Surname
    private String surname;

    @OneToOne(cascade = CascadeType.ALL)
    private File image;

    public UserInfo(Long id, User user, @Name String name, @Surname String surname) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.surname = surname;
    }

    public UserInfo(@Name String name, @Surname String surname) {
        this.name = name;
        this.surname = surname;
    }
}
