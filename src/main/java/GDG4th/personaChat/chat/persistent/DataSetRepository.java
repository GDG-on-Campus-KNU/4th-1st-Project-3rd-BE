package GDG4th.personaChat.chat.persistent;

import GDG4th.personaChat.chat.domain.DataSet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DataSetRepository extends MongoRepository<DataSet, String > {
}
