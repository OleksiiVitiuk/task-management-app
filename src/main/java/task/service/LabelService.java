package task.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import task.dto.LabelCreateRequestDto;
import task.dto.LabelDto;

public interface LabelService {
    Page<LabelDto> getLabels(Pageable pageable);

    LabelDto createLabels(LabelCreateRequestDto createRequestDto);

    LabelDto updateLabel(Long id, LabelCreateRequestDto createRequestDto);

    void deleteLabel(Long id);
}
