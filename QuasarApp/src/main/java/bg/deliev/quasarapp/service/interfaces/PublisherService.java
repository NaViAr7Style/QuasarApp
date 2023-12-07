package bg.deliev.quasarapp.service.interfaces;

import bg.deliev.quasarapp.model.dto.AddPublisherDTO;
import bg.deliev.quasarapp.model.dto.PublisherSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PublisherService {

    Page<PublisherSummaryDTO> getAllPublishers(Pageable pageable);

    void deletePublisher(Long id);

    String getPublisherName(long id);

    void addPublisher(AddPublisherDTO addPublisherDTO);
}
