package org.pds.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.pds.model.Frequentation;
import org.pds.repository.FrequentationRepository;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class FrequentationServiceImplTest {


    @Mock
    private FrequentationRepository frequentationRepository;
    @InjectMocks
    private FrequentationImplService frequentationService;
    @Mock
    Frequentation frequentationMock;



    @Test
    public void when_save_frequentation_return_frequentation() {
        Frequentation frequentation = new Frequentation(null,"111","zara","Entree",null);
        when(frequentationRepository.save(any(Frequentation.class))).thenReturn(frequentation);
        Frequentation returned = frequentationService.save(frequentation);
        ArgumentCaptor<Frequentation> frequentationArgument = ArgumentCaptor.forClass(Frequentation.class);
        Mockito.verify(frequentationRepository, times(1)).save(frequentationArgument.capture());
        verifyNoMoreInteractions(frequentationRepository);
        assertEquals(frequentation, returned);
    }

    @Test
    public void findAll_return_all_frequentations() {
        Iterable<Frequentation> frequentations = new ArrayList<Frequentation>();
        when(frequentationRepository.findAll(any(Sort.class))).thenReturn(frequentations);
        Iterable<Frequentation> returned = frequentationService.findAll();
        assertEquals(frequentations, returned);
    }

    @Test
    public void findById_return_frequentation() {
        Frequentation frequentation = new Frequentation(null,"111","zara","Entree",null);
        when(frequentationRepository.findById("111")).thenReturn(Optional.of(frequentation));
        Optional<Frequentation> returned = frequentationService.findById("111");
        verify(frequentationRepository, times(1)).findById("111");
        verifyNoMoreInteractions(frequentationRepository);
        assertEquals(frequentation, returned.get());
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = NullPointerException.class)
    public void findById_return_false() {
        frequentationMock = new Frequentation(null,"111","zara","Entree",null);
        when(frequentationRepository.findById("111")).thenReturn(Optional.of(frequentationMock));
        when(frequentationRepository.findById("222")).thenThrow(NullPointerException.class);

        Optional<Frequentation> returned = frequentationService.findById("222");
    }
}
