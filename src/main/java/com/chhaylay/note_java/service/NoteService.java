package com.chhaylay.note_java.service;

import com.chhaylay.note_java.dto.NoteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoteService {
    Page<NoteDto.ResponseAll> getAllNotes(Pageable pageable);
    NoteDto.ResponseDetail getNoteById(Long id);
    NoteDto.ResponseDetail createNote(NoteDto.Request request);
    NoteDto.ResponseDetail updateNote(Long id, NoteDto.Request request);
    void deleteNote(Long id);
}