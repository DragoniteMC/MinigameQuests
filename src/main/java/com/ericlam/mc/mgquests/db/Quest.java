package com.ericlam.mc.mgquests.db;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@IdClass(Quest.Quester.class)
public class Quest {

    @Id
    public String quest;

    @Id
    @Column(columnDefinition = "varchar(40)")
    @Type(type = "uuid-char")
    public UUID user;

    public long lastFinished;

    public long lastStarted;


    public static class Quester implements Serializable {


        public String quest;

        @Column(columnDefinition = "varchar(40)")
        @Type(type = "uuid-char")
        public UUID user;

        public Quester() {
        }

        public Quester(String quest, UUID user) {
            this.quest = quest;
            this.user = user;
        }
    }

}
