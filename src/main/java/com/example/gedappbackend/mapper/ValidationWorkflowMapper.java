package com.example.gedappbackend.mapper;

@Mapper(componentModel = "spring")
public interface ValidationWorkflowMapper {

    @Mapping(target = "documentId", source = "document.id")
    @Mapping(target = "documentName", source = "document.name")
    @Mapping(target = "submitterId", source = "submitter.id")
    @Mapping(target = "submitterName", source = "submitter.username")
    @Mapping(target = "validatorId", source = "validator.id")
    @Mapping(target = "validatorName", source = "validator.username")
    ValidationWorkflowResponse toResponse(ValidationWorkflow workflow);

    List<ValidationWorkflowResponse> toResponseList(List<ValidationWorkflow> workflows);
}
