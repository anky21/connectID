package me.anky.connectid.details;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import me.anky.connectid.data.DetailsDataSource;

public class DetailsActivityPresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    DetailsDataSource detailsDataSource;

    @Mock
    DetailsActivityView view;

    private DetailsActivityPresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new DetailsActivityPresenter(
                view, detailsDataSource, Schedulers.trampoline());
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
    }

    @Test
    public void shouldDeliverDatabaseIdToModel() {

        int databaseIdFromUri = 1;
        Mockito.when(view.getConnectionToDelete()).thenReturn(databaseIdFromUri);

        presenter.deliveDatabaseIdtoDelete();

        Mockito.verify(detailsDataSource).deleteConnection(databaseIdFromUri);

    }
}
