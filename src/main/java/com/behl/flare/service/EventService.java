package com.behl.flare.service;

import com.behl.flare.entity.EventCard;
import com.behl.flare.entity.User;
import com.behl.flare.enums.Roles;
import com.behl.flare.exception.AccessDeniedByRoleException;
import com.behl.flare.mappers.EventCardMapper;
import com.behl.flare.repository.EventCardJpaRepository;
import com.behl.flare.repository.UserJpaRepository;
import jakarta.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.behl.flare.dto.eventcard.EventCardRequest;
import com.behl.flare.dto.eventcard.EventCardResponse;
import com.behl.flare.entity.Event;
import com.behl.flare.exception.InvalidTaskIdException;
import com.behl.flare.exception.UserRoleViolationException;
import com.behl.flare.utility.AuthenticatedUserIdProvider;
import com.behl.flare.utility.DateUtility;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    private final Firestore firestore;
    private final DateUtility dateUtility;
    private final AuthenticatedUserIdProvider authenticatedUserIdProvider;
    private final EventCardJpaRepository eventCardJpaRepository;
    private final EventCardMapper eventCardMapper;
    private final UserService userService;
    private final UserJpaRepository userJpaRepository;


    @Transactional
    public EventCardResponse getEventCard(Long taskId) {
        EventCard eventCard = eventCardJpaRepository.findById(taskId).orElseThrow();
        EventCardResponse response = eventCardMapper.toResponse(eventCard);
        return response;
    }


    public Page<EventCardResponse> getEventCards(Pageable pageable) {
        Page<EventCard> all = eventCardJpaRepository.findAll(pageable);
        Page<EventCardResponse> response = all.map(eventCardMapper::toResponse);
        return response;
    }


    /**
     * Получение мероприятий с пейджингом.
     * Для пользователя с ролью CREATOR возвращаются только его мероприятия.
     * Для пользователя с полью ADMIN возвращаются все мероприятия.
     *
     * @param pageable - пейджинг
     * @return страницу с DTO мероприятий
     */
    @Transactional
    public Page<EventCardResponse> getEventCardsByCreator(Pageable pageable) {
        User currentUser = userService.getCurrentUser();
        Roles role = currentUser.getRole();

        Page<EventCard> events;
        switch (role) {
            case ROLE_ADMIN -> events = eventCardJpaRepository.findAll(pageable);
            case ROLE_CREATOR -> events = eventCardJpaRepository.findAllByCreatorId(currentUser.getId(), pageable);
            default -> throw new AccessDeniedByRoleException();
        }

//        Page<EventCard> all = eventCardJpaRepository.findAll(pageable);
        Page<EventCardResponse> response = events.map(eventCardMapper::toResponse);
        return response;
    }


    @Transactional
    public void createEventCard(@NonNull final EventCardRequest request) {

//        String firebaseId = userService.getCurrentUser().getFirebaseId();
        Long userId = userService.getCurrentUser().getId();
        request.setCreatorId(userId);

        EventCard eventCard = eventCardMapper.toEntity(request);
        User currentUser = userService.getCurrentUser();
        eventCard.setCreator(currentUser);
        eventCardJpaRepository.save(eventCard);
    }


    @Transactional
    public void updateEventCard(Long eventId, EventCardRequest request) {
        User currentUser = userService.getCurrentUser();
        EventCard eventCard = eventCardJpaRepository.findById(eventId).orElseThrow();
        if (currentUser.isCreatorOf(eventCard) || currentUser.isAdmin()) {
            eventCardMapper.updateEntityFromRequest(request, eventCard);
            eventCardJpaRepository.save(eventCard);
        }
    }


    /**
     * CREATOR может удалять только свои карточки
     */
    @Transactional
    public void delete(Long eventId) {
        User currentUser = userService.getCurrentUser();
        EventCard eventCard = eventCardJpaRepository.findById(eventId).orElseThrow();
        if (currentUser.isCreatorOf(eventCard) || currentUser.isAdmin()) {
            eventCardJpaRepository.delete(eventCard);
        }
    }

    /**
     * Verifies if the given task belongs to the current authenticated user.
     *
     * @param event the record to be verified for ownership.
     * @throws IllegalArgumentException        if provided argument is {@code null}
     * @throws UserRoleViolationException on validation failure
     */
/*
    private void verifyTaskOwnership(@NonNull final Event event) {
        final var userId = authenticatedUserIdProvider.getUserId();
        final var taskBelongsToUser = event.getCreatedBy().equals(userId);
        if (Boolean.FALSE.equals(taskBelongsToUser)) {
            throw new TaskOwnershipViolationException();
        }
    }
*/

    /**
     * Retrieves a task document from Firestore database corresponding to
     * its document ID.
     *
     * @param taskId the ID of the task document to retrieve
     * @return the DocumentSnapshot representing the retrieved task document
     * @throws InvalidTaskIdException if no task exists corresponding to given taskId
     */
    @SneakyThrows
    private DocumentSnapshot get(@NonNull final String taskId) {
        final var retrievedDocument = firestore.collection(Event.name()).document(taskId).get().get();
        final var documentExists = retrievedDocument.exists();
        if (Boolean.FALSE.equals(documentExists)) {
            throw new InvalidTaskIdException("No task exists in the system with provided-id");
        }
        return retrievedDocument;
    }


    /**
     * Добавление мероприятия в избранные текущему юзеру
     */
    @Transactional
    public void addEventToFavorite(Long eventId) {
        EventCard eventCard = eventCardJpaRepository.findById(eventId).orElseThrow();
        User currentUser = userService.getCurrentUser();
        currentUser.getFavoriteEvents().add(eventCard);
        userJpaRepository.save(currentUser);
        log.info("User: {}. Added favorite event: {}", currentUser.getId(), eventCard.getId());
    }


    /**
     * Удаление мероприятия из избранного у текущего юзера
     */
    @Transactional
    public void removeEventFromFavorite(Long eventCardId) {
        User currentUser = userService.getCurrentUser();
        currentUser.getFavoriteEvents().removeIf(eventCard1 -> eventCard1.getId().equals(eventCardId));
        userJpaRepository.save(currentUser);
        log.info("User: {}. Removed favorite event: {}", currentUser.getId(), eventCardId);
    }


    /**
     * Добавление мероприятия в запланированные текущему юзеру
     */
    @Transactional
    public void addEventToPlanned(Long eventCardId) {
        EventCard eventCard = eventCardJpaRepository.findById(eventCardId).orElseThrow();
        User currentUser = userService.getCurrentUser();
        currentUser.getPlannedEvents().add(eventCard);
        userJpaRepository.save(currentUser);
        log.info("User: {}. Added planned event: {}", currentUser.getId(), eventCard.getId());
    }


    /**
     * Удаление мероприятия из избранного у текущего юзера
     */
    @Transactional
    public void removeEventFromPlanned(Long eventCardId) {
        User currentUser = userService.getCurrentUser();
        currentUser.getPlannedEvents().removeIf(eventCard1 -> eventCard1.getId().equals(eventCardId));
        userJpaRepository.save(currentUser);
        log.info("User: {}. Removed planned event: {}", currentUser.getId(), eventCardId);
    }

}
