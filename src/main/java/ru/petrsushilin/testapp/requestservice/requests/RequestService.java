package ru.petrsushilin.testapp.requestservice.requests;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.petrsushilin.testapp.requestservice.global.exceptions.ChangeStatusException;
import ru.petrsushilin.testapp.requestservice.global.exceptions.IdentifierMismatchException;
import ru.petrsushilin.testapp.requestservice.requests.dto.RequestCreationDTO;
import ru.petrsushilin.testapp.requestservice.requests.dto.RequestMessageDTO;
import ru.petrsushilin.testapp.requestservice.requests.dto.RequestResponseDTO;
import ru.petrsushilin.testapp.requestservice.requests.dto.RequestSetStageDTO;
import ru.petrsushilin.testapp.requestservice.requests.enums.Stage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Petr Sushilin
 * @version 1.0
 * @since 05.05.2024
 */
@Service
public class RequestService {
    private final RequestRepository requestRepository;

    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Value("${app.pagination.defaultPageSize}")
    private int defaultPageSize;

    /*------------------------------------------- HIDE ------------------------------------------*/

    private Pageable getPageable(int page, String sortDir) {
        Sort sort = getSortType(sortDir);
        return PageRequest.of(page, defaultPageSize, sort);
    }

    private Sort getSortType(String sortDir) {
        return Sort
                .by(sortDir.equalsIgnoreCase("reverse") ?
                        Sort.Direction.DESC : Sort.Direction.ASC,
                        "createdAt");
    }

    private Request changeRequestStage(Long requestID, Long userID, Stage newStage) {
        return requestRepository
                .findById(requestID)
                .map(request -> {
                    if (!request.getUser().getId().equals(userID))
                        throw new IdentifierMismatchException("User id does not match with request author's ID.");
                    request.setStage(newStage);
                    requestRepository.save(request);
                    return request;
                })
                .orElseThrow(() -> new IllegalArgumentException("Request with id " + requestID + " not found"));
    }

    /*------------------------------------------- GENERAL ------------------------------------------*/

    @Transactional(readOnly = true)
    public RequestResponseDTO getRequest(Long requestID, String role) {
        Request request = requestRepository.findById(requestID)
                .orElseThrow(() -> new IdentifierMismatchException("Request with id " + requestID + " not found"));

        RequestResponseDTO requestResponseDTO = RequestMapper.INSTANCE.toRequestResponseDTO(request);

        // set description for operator
        if (role.equals("ROLE_OPERATOR")) {
            setOperatorDescriptions(requestResponseDTO);
        }

        return requestResponseDTO;
    }

    /*------------------------------------------- USER METHODS ------------------------------------------*/

    /**
     * Method create new request and response created request with status "draft".
     *
     * @param requestCreationDTO: Long authorID, String description
     * @return requestResponseDTO: Long requestID, LocalDateTime dateCreated, Long authorID, String status, String description
     */
    @Transactional(rollbackFor = Exception.class)
    public RequestResponseDTO createRequest(RequestCreationDTO requestCreationDTO) {
        Request request = RequestMapper.INSTANCE.toEntity(requestCreationDTO);

        request.setCreatedAt(LocalDate.now());
        request.setStage(Stage.DRAFT);

        return RequestMapper.INSTANCE.toRequestResponseDTO(requestRepository.save(request));
    }

    @Transactional(readOnly = true)
    public List<RequestResponseDTO> getUserRequests(Long userID, String sortDir, int page) {
        return requestRepository
                .findRequestsByUserID(userID, getPageable(page, sortDir))
                .stream()
                .map(RequestMapper.INSTANCE::toRequestResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public RequestResponseDTO updateRequestMessage(RequestMessageDTO requestMessageDTO) {
        return requestRepository
                .findByIdForUpdate(requestMessageDTO.getRequestID())
                .map(request -> {
                    if (!request.getUser().getId().equals(requestMessageDTO.getAuthorID()))
                        throw new IdentifierMismatchException("User id does not match with request author's ID.");
                    request.setDescription(requestMessageDTO.getDescription());
                    return RequestMapper.INSTANCE.toRequestResponseDTO(requestRepository.save(request));
                })
                .orElseThrow(() -> new IdentifierMismatchException("Request with id " + requestMessageDTO.getRequestID() + " not found"));
    }

    @Transactional(rollbackFor = Exception.class)
    public RequestResponseDTO submitRequest(RequestSetStageDTO reqDTO) {
        if (reqDTO.getCurrentStage().equals(String.valueOf(Stage.DRAFT))) {
            Request request = changeRequestStage(reqDTO.getRequestID(), reqDTO.getUserID(), Stage.SUBMITTED);
            return RequestMapper.INSTANCE.toRequestResponseDTO(request);
        }
        else
            throw new ChangeStatusException("Status cannot to be change to " + reqDTO.getCurrentStage() + ".");
    }

    // --------------------------------------- OPERATOR METHODS ---------------------------------------

    @Transactional(readOnly = true)
    public List<RequestResponseDTO> getOperatorRequests(String sortDir, int page) {
        List<RequestResponseDTO> requests = requestRepository
                .findRequestsByStage(Stage.SUBMITTED, getPageable(page, sortDir))
                .stream()
                .map(RequestMapper.INSTANCE::toRequestResponseDTO)
                .collect(Collectors.toList());

        // set descriptions for operator
        requests.forEach(this::setOperatorDescriptions);

        return requests;
    }


    /** по контракту мы ожидаем от пользователя передачи данных в формате:
     * Только имя, только фамлиию, либо имя и фамилия.
     * В сочетании записи фамлиия имя вызовет ошибку.
     * @param fullName
     * @param sortDir
     * @param page
     * @return List<Request>
     */
    @Transactional(readOnly = true)
    public List<RequestResponseDTO> getPersonRequests(String fullName, String sortDir, int page) {
        String[] nameParts = fullName.split(" ");
        String namePart1 = nameParts[0];
        String namePart2 = nameParts.length > 1 ? nameParts[1] : "";

        List<RequestResponseDTO> requests = requestRepository
                .findRequestsByPersonName(namePart1, namePart2, getPageable(page, sortDir))
                .stream()
                .map(RequestMapper.INSTANCE::toRequestResponseDTO)
                .collect(Collectors.toList());

        // set descriptions for operator
        requests.forEach(this::setOperatorDescriptions);

        return requests;
    }

    @Transactional(rollbackFor = Exception.class)
    public RequestResponseDTO approveRequest(RequestSetStageDTO reqDTO) {
        String checkStage = String.valueOf(Stage.SUBMITTED);

        if (reqDTO.getCurrentStage().equals(checkStage)) {
            Request request = changeRequestStage(reqDTO.getRequestID(), reqDTO.getUserID(), Stage.APPROVED);
            return RequestMapper.INSTANCE.toRequestResponseDTO(request);
        }
        else
            throw new ChangeStatusException("Status cannot to be change to " + reqDTO.getCurrentStage() + ".");
    }

    @Transactional(rollbackFor = Exception.class)
    public RequestResponseDTO rejectRequest(RequestSetStageDTO reqDTO) {
        String checkStage = String.valueOf(Stage.SUBMITTED);

        if (reqDTO.getCurrentStage().equals(checkStage)) {
            Request request = changeRequestStage(reqDTO.getRequestID(), reqDTO.getUserID(), Stage.REJECTED);
            return RequestMapper.INSTANCE.toRequestResponseDTO(request);
        }
        else
            throw new ChangeStatusException("Status cannot to be change to " + reqDTO.getCurrentStage() + ".");
    }

    private void setOperatorDescriptions(RequestResponseDTO request) {
        request.setDescription(
                addDashesToString(request.getDescription())
        );
    }

    private String addDashesToString(String input) {
        return input.chars().mapToObj(c -> (char) c + "-")
                .collect(Collectors.joining())
                .replaceAll("-$", "");
    }
}
