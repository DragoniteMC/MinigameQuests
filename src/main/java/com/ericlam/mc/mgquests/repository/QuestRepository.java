package com.ericlam.mc.mgquests.repository;

import com.ericlam.mc.mgquests.db.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuestRepository extends JpaRepository<Quest, Quest.Quester> {

    List<Quest> findByUser(UUID user);

}