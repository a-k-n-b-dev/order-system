package dev.aknb.ordersystem.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class Filter {

    private String searchText = "";
    private int pageNumber = 0;
    @Min(value = 1, message = "INVALID_FILTER_SIZE")
    private int pageSize = 16;

    private Sort.Direction sort = Sort.Direction.DESC;

    @JsonIgnore
    public Pageable getPageable() {

        return PageRequest.of(
                this.pageNumber,
                this.pageSize,
                Sort.by(sort, "lastModifiedDate")
        );
    }
}
