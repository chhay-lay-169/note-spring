package com.chhaylay.note_java.service.impl;

import com.chhaylay.note_java.dto.NoteDto;
import com.chhaylay.note_java.exception.ResourceNotFoundException;
import com.chhaylay.note_java.model.Note;
import com.chhaylay.note_java.model.User;
import com.chhaylay.note_java.repository.NoteRepository;
import com.chhaylay.note_java.security.UserPrincipal;
import com.chhaylay.note_java.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<NoteDto.ResponseAll> getAllNotes(Pageable pageable) {
        Long userId = getCurrentUserId();
        return noteRepository.findAllByUserId(userId, pageable)
                .map(note -> new NoteDto.ResponseAll(
                    note.getId(),
                    note.getTitle(),
                    note.getUpdatedAt()
                )
            );
    }

    @Override
    @Transactional(readOnly = true)
    public NoteDto.ResponseDetail getNoteById(Long id) {
        Long userId = getCurrentUserId();
        Note note = noteRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Target Note..."));
        
        return mapToResponseDetail(note);
    }

    @Override
    @Transactional
    public NoteDto.ResponseDetail createNote(NoteDto.Request request) {
        Long userId = getCurrentUserId();
        Note note = Note.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(User.builder().id(userId).build())
                .build();

        return mapToResponseDetail(noteRepository.save(note));
    }

    @Override
    @Transactional
    public NoteDto.ResponseDetail updateNote(Long id, NoteDto.Request request) {
        Long userId = getCurrentUserId();
        Note note = noteRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Target Note entity with ID [" + id + "] does not exist."));
        
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        return mapToResponseDetail(noteRepository.save(note));
    }

    @Override
    @Transactional
    public void deleteNote(Long id) {
        Long userId = getCurrentUserId();
        Note note = noteRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Target Note entity with ID [" + id + "] does not exist."));
        
        noteRepository.delete(note);
    }

    private Long getCurrentUserId() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getId();
    }

    private NoteDto.ResponseDetail mapToResponseDetail(Note note) {
        return new NoteDto.ResponseDetail(
            note.getId(),
            note.getTitle(),
            note.getContent(),
            note.getUpdatedAt()
        );
    }
}
