package com.drevish.social.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User sender;

    private String text;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    private LocalDate sendDate;

    public Message(User sender, String text, Chat chat, LocalDate sendDate) {
        this.sender = sender;
        this.text = text;
        this.chat = chat;
        this.sendDate = sendDate;
    }
}
