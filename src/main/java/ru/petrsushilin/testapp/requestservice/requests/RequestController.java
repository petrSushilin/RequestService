package ru.petrsushilin.testapp.requestservice.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.petrsushilin.testapp.requestservice.global.exceptions.ChangeStatusException;
import ru.petrsushilin.testapp.requestservice.global.exceptions.IdentifierMismatchException;
import ru.petrsushilin.testapp.requestservice.requests.dto.RequestCreationDTO;
import ru.petrsushilin.testapp.requestservice.requests.dto.RequestMessageDTO;
import ru.petrsushilin.testapp.requestservice.requests.dto.RequestResponseDTO;
import ru.petrsushilin.testapp.requestservice.requests.dto.RequestSetStageDTO;

import java.util.List;
import java.util.Optional;

/**
 * The controller for handling requests methods.
 * This .class using for:
 * - create new request by user.
 * - change description of request before sending for confirm by user.
 * - send request to operator for confirm by user.
 * - approve by operator.
 * - reject by operator.
 * - searching request by ID by user and operator.
 * - searching all requests with "draft" stage created by the user himself and ordering requests by date_created in ascending or descending order.
 * - searching all requests with status "submitted" by operator and ordering requests by date_created in ascending or descending order.
 * - searching all requests by full name or some part with any status by operator and ordering requests by date_created in ascending or descending order.
 *
 * @author Petr Sushilin
 * @version 1.0
 * @since 05.05.2024
 */
@RestController
@RequestMapping("/request")
public class RequestController {
    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    /*------------------------------------------ USER ------------------------------------------*/
    /**
     * Handle using for create new request and response created request with status "draft".
     * Required Role: User.
     * Required HTTP method: POST.
     * Correct response status code: 201 CREATED.
     * @param requestCreationDTO consists of {@link Long} as authorID whose request will be created, {@link String} as description of request.
     * @return ResponseEntity {@link RequestResponseDTO}
     * @throws IdentifierMismatchException if authorID not found.
     */
    @Secured({"ROLE_USER"})
    @PostMapping("/create")
    public ResponseEntity<?> createRequest(@RequestBody RequestCreationDTO requestCreationDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(requestService.createRequest(requestCreationDTO));
    }

    /**
     * Handle using for searching all requests by userID with any status.
     * It can be sorted by date_created in ascending or descending order.
     * Response paged by 5 elements.
     * Required Role: User.
     * Required HTTP method: GET.
     * Correct response status code: 200 OK.
     * @param userID consists of {@link Long} the ID of user whose list of requests will be searching.
     * @param sort consists of {@link String} type of sorting: "default" as ASC or "reverse" as DESC.
     * @param page consists of {@link int} number of page for pagination.
     * @return ResponseEntity {@link List} of {@link RequestResponseDTO}
     * @throws IdentifierMismatchException if authorID not found.
     * @throws IllegalArgumentException if sort or page not correct.
     */
    @Secured({"ROLE_USER"})
    @GetMapping("/list/id{userID}")
    public ResponseEntity<List<?>> getUserRequests(@PathVariable("userID") Long userID,
                                                   @RequestParam(value = "sort", required = true, defaultValue = "default") String sort,
                                                   @RequestParam(value = "page", required = true, defaultValue = "0") int page) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(requestService.getUserRequests(userID, sort, page));
    }

    /**
     * Handle using for change description of request.
     * Required correct params: it uses check of authorID and request presence.
     * Required Role: User.
     * Required HTTP method: GET.
     * Correct response status code: 200 OK.
     * @param requestMessageDTO consists of {@link Long} as requestID, {@link Long} as authorID, {@link String} as description of request.
     * @return ResponseEntity {@link RequestResponseDTO}
     * @throws IdentifierMismatchException if request not found or authorID not equals to request authorID.
     */
    @Secured({"ROLE_USER"})
    @PostMapping("/change-description")
    public ResponseEntity<?> updateRequestMessage(@RequestBody RequestMessageDTO requestMessageDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(requestService.updateRequestMessage(requestMessageDTO));
    }

    /**
     * Handle using for sending request to operator by changing status to "submitted".
     * Required has correct stage inside DTO and request presence.
     * Required Role: User.
     * Required HTTP method: POST.
     * Correct response status code: 200 OK.
     * @param requestSetStageDTO consists of {@link Long} as requestID, {@link Long} as authorID, {@link String} as description of request.
     * @return ResponseEntity {@link RequestResponseDTO}
     * @throws IdentifierMismatchException if request not found or authorID not equals to request authorID.
     * @throws ChangeStatusException if request status not "draft".
     */
    @Secured({"ROLE_USER"})
    @PostMapping("/send")
    public ResponseEntity<?> sendRequestToOperator(@RequestBody RequestSetStageDTO requestSetStageDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(requestService.submitRequest(requestSetStageDTO))   ;
    }


    /*------------------------------------------- OPERATOR ------------------------------------------*/

    /**
     * Handle using for searching all requests with status "submitted".
     * It can be sorted by date_created in ascending or descending order.
     * The description of request will have dashed between symbols.
     * Response paged by 5 elements.
     * Required Role: Operator.
     * Required HTTP method: GET.
     * Correct response status code: 200 OK.
     * @param sort consists of {@link String} type of sorting: "default" as ASC or "reverse" as DESC.
     * @param page consists of {@link int} number of page for pagination.
     * @return ResponseEntity {@link List} of {@link RequestResponseDTO}
     * @throws IllegalArgumentException if sort or page not correct.
     */
    @Secured({"ROLE_OPERATOR"})
    @GetMapping("/all-requests")
    public ResponseEntity<List<?>> getOperatorRequests(@RequestParam(value = "sort", required = true, defaultValue = "default") String sort,
                                                 @RequestParam(value = "page", required = true, defaultValue = "0") int page) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(requestService.getOperatorRequests(sort, page));
    }

    /**
     * Handle using for searching all requests by full name or some part with any status.
     * It works same with name/surname and surname/name formats and with only part of full name by the way it have worse performance.
     * The description of request will have dashed between symbols.
     * It can be sorted by date_created in ascending or descending order.
     * Response paged by 5 elements.
     * Required Role: Operator.
     * Required HTTP method: GET.
     * Correct response status code: 200 OK.
     * @param name consists of {@link String} as full name or some part of it.
     * @param sort consists of {@link String} type of sorting: "default" as ASC or "reverse" as DESC.
     * @param page consists of {@link int} number of page for pagination.
     * @return ResponseEntity {@link List} of {@link RequestResponseDTO}.
     * @throws IllegalArgumentException if sort or page not correct.
     * @throws IdentifierMismatchException if authorID not found.
     */
    @Secured({"ROLE_OPERATOR"})
    @GetMapping("/requests/{name}")
    public ResponseEntity<?> getPersonRequests(@PathVariable("name") String name,
                                               @RequestParam(value = "sort", required = true, defaultValue = "default") String sort,
                                               @RequestParam(value = "page", required = true, defaultValue = "0") int page) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(requestService.getPersonRequests(name, sort, page));
    }

    /**
     * Handle using for approve request by operator and changing status to "approved".
     * Required has correct stage inside DTO and request presence.
     * Required Role: Operator.
     * Required HTTP method: POST.
     * Correct response status code: 200 OK.
     * @param requestSetStageDTO consists of {@link Long} as requestID, {@link Long} as authorID, {@link String} as description of request.
     * @return ResponseEntity {@link RequestResponseDTO}
     * @throws IdentifierMismatchException if request not found or authorID not equals to request authorID.
     * @throws ChangeStatusException if request status not "submitted".
     */
    @Secured({"ROLE_OPERATOR"})
    @PostMapping("/approve")
    public ResponseEntity<?> approveRequest(@RequestBody RequestSetStageDTO requestSetStageDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(requestService.approveRequest(requestSetStageDTO));
    }

    /**
     * Handle using for approve request by operator and changing status to "rejected".
     * Required has correct stage inside DTO and request presence.
     * Required Role: Operator.
     * Required HTTP method: POST.
     * Correct response status code: 200 OK.
     * @param requestSetStageDTO consists of {@link Long} as requestID, {@link Long} as authorID, {@link String} as description of request.
     * @return ResponseEntity {@link RequestResponseDTO}
     * @throws IdentifierMismatchException if request not found or authorID not equals to request authorID.
     * @throws ChangeStatusException if request status not "submitted".
     */
    @Secured({"ROLE_OPERATOR"})
    @PostMapping("/reject")
    public ResponseEntity<?> rejectRequest(@RequestBody RequestSetStageDTO requestSetStageDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(requestService.rejectRequest(requestSetStageDTO));
    }

    /*------------------------------------------- GENERAL ------------------------------------------*/

    /**
     * Handle using for searching request by requestID.
     * The description of request will have dashed between symbols.
     * Required Role: User, Operator.
     * Required HTTP method: GET.
     * Correct response status code: 200 OK.
     * @param requestID consists of {@link Long}.
     * @return ResponseEntity {@link List} of {@link RequestResponseDTO}.
     * @throws IllegalArgumentException if sort or page not correct.
     * @throws IdentifierMismatchException if authorID not found.
     */
    @Secured({"ROLE_USER", "ROLE_OPERATOR"})
    @GetMapping("/{requestID}")
    public ResponseEntity<?> getRequest(@PathVariable("requestID") Long requestID) {
        //extract role from authentication
        String role = extractRole(SecurityContextHolder.getContext().getAuthentication());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(requestService.getRequest(requestID, role));
    }

    /**
     * Hide method which help to extract role from authentication.
     */
    private String extractRole(Authentication authentication) {
        Optional<String> operatorRole = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> auth.equals("ROLE_OPERATOR"))
                .findFirst();
        if (operatorRole.isPresent())
            return "ROLE_OPERATOR";

        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(null);
    }
}
