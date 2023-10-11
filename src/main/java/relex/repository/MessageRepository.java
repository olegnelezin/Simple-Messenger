package relex.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import relex.model.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findMessageByUserIdWhoSend(long id);
    List<Message> findMessageByUserIdWhoRecieve(long id);
    List<Message> findMessageByUserIdWhoSendOrUserIdWhoRecieve(long id1, long id2);
}
