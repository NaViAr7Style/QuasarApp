package bg.deliev.quasarapp.service.interfaces;

import bg.deliev.quasarapp.model.dto.AddPublisherDTO;
import bg.deliev.quasarapp.model.dto.PublisherSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PublisherService {

    Page<PublisherSummaryDTO> getAllPublishers(Pageable pageable);

    List<String> getAllPublisherNames();

    void deletePublisher(Long id);

    String getPublisherName(long id);

    void addPublisher(AddPublisherDTO addPublisherDTO);

    List<PublisherSummaryDTO> getAll();
}
