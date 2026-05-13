package com.example.gedappbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {
    private String query;
    private List<String> tags;
    private String documentType;
    private String status;
    private UUID ownerId;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private int page;
    private int size;
}
