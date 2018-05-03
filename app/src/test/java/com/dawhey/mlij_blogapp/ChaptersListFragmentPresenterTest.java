package com.dawhey.mlij_blogapp;

import com.dawhey.mlij_blogapp.Models.Chapter;
import com.dawhey.mlij_blogapp.Models.Posts;
import com.dawhey.mlij_blogapp.Presenters.ChaptersListFragmentPresenter;
import com.dawhey.mlij_blogapp.Repositories.ChaptersRepository;
import com.dawhey.mlij_blogapp.Views.ChaptersListView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChaptersListFragmentPresenterTest {

    private final Posts someElements = new Posts(Arrays.asList(new Chapter(), new Chapter(), new Chapter()));

    @Mock
    ChaptersRepository chaptersRepository;

    @Mock
    ChaptersListView chaptersListView;

    ChaptersListFragmentPresenter presenter;

    @Before
    public void setUp() {
        presenter = new ChaptersListFragmentPresenter(chaptersListView, chaptersRepository, Schedulers.trampoline());

        RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(Scheduler scheduler) {
                return Schedulers.trampoline();
            }
        });
    }

    @Test
    public void shouldDisplayChaptersView() {
        when(chaptersRepository.getAllChapters()).thenReturn((Single.just(someElements)));

        presenter.loadChapters();

        verify(chaptersListView).showChapters(someElements.getChapters());
        verify(chaptersListView).updateChapters(someElements.getChapters());
    }

    @Test
    public void shouldDisplayErrorView() {
        when(chaptersRepository.getAllChapters()).thenReturn(Single.<Posts>error(new Throwable("Error")));
        presenter.loadChapters();

        verify(chaptersListView).showError();
    }

    @After
    public void cleanUp() {
        RxJavaPlugins.reset();
    }


}
