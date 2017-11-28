package com.wrapper.spotify.requests;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.TestUtil;
import com.wrapper.spotify.objects.AlbumSimplified;
import com.wrapper.spotify.objects.ObjectType;
import com.wrapper.spotify.objects.Paging;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.*;

@RunWith(MockitoJUnitRunner.class)
public class AlbumSearchRequestTest {

  @Test
  public void shouldGetAlbumsResult_async() throws Exception {
    final Api api = Api.DEFAULT_API;

    final AlbumSearchRequest request = api.searchAlbums("tania bowra")
            .setHttpManager(TestUtil.MockedHttpManager.returningJson("search-album.json"))
            .build();

    final CountDownLatch asyncCompleted = new CountDownLatch(1);

    final SettableFuture<Paging<AlbumSimplified>> searchResultFuture = request.getAsync();

    Futures.addCallback(searchResultFuture, new FutureCallback<Paging<AlbumSimplified>>() {
      @Override
      public void onSuccess(Paging<AlbumSimplified> albumSearchResult) {
        assertEquals("https://api.spotify.com/v1/search?query=tania%2Bbowra&offset=0&limit=20&type=album", albumSearchResult.getHref());
        assertEquals(20, albumSearchResult.getLimit());
        assertEquals(0, albumSearchResult.getOffset());
        assertNull(albumSearchResult.getNext());
        assertNull(albumSearchResult.getPrevious());
        assertEquals(1, albumSearchResult.getTotal());

        List<AlbumSimplified> albums = albumSearchResult.getItems();
        assertEquals(1, albums.size());

        AlbumSimplified firstAlbum = albums.get(0);
        assertEquals("https://open.spotify.com/album/6akEvsycLGftJxYudPjmqK", firstAlbum.getExternalUrls().get("spotify"));
        assertEquals("https://api.spotify.com/v1/albums/6akEvsycLGftJxYudPjmqK", firstAlbum.getHref());
        assertEquals("6akEvsycLGftJxYudPjmqK", firstAlbum.getId());
        assertEquals("Place In The Sun", firstAlbum.getName());
        assertEquals(ObjectType.ALBUM, firstAlbum.getType());
        assertEquals("spotify:album:6akEvsycLGftJxYudPjmqK", firstAlbum.getUri());
        assertNotNull(firstAlbum.getAvailableMarkets());
        assertFalse(firstAlbum.getAvailableMarkets().isEmpty());

        asyncCompleted.countDown();
      }

      @Override
      public void onFailure(Throwable throwable) {
        fail("Failed to resolve future");
      }
    });

    asyncCompleted.await(1, TimeUnit.SECONDS);
  }

  @Test
  public void shouldGetAlbumsResult_sync() throws Exception {
    final Api api = Api.DEFAULT_API;

    final AlbumSearchRequest request = api.searchAlbums("tania bowra")
            .setHttpManager(TestUtil.MockedHttpManager.returningJson("search-album.json"))
            .build();

    final Paging<AlbumSimplified> albumSearchResult = request.get();
    assertEquals("https://api.spotify.com/v1/search?query=tania%2Bbowra&offset=0&limit=20&type=album", albumSearchResult.getHref());
    assertEquals(20, albumSearchResult.getLimit());
    assertEquals(0, albumSearchResult.getOffset());
    assertNull(albumSearchResult.getNext());
    assertNull(albumSearchResult.getPrevious());
    assertEquals(1, albumSearchResult.getTotal());

    final List<AlbumSimplified> albums = albumSearchResult.getItems();
    assertEquals(1, albums.size());

    AlbumSimplified firstAlbum = albums.get(0);
    assertEquals("https://open.spotify.com/album/6akEvsycLGftJxYudPjmqK", firstAlbum.getExternalUrls().get("spotify"));
    assertEquals("https://api.spotify.com/v1/albums/6akEvsycLGftJxYudPjmqK", firstAlbum.getHref());
    assertEquals("6akEvsycLGftJxYudPjmqK", firstAlbum.getId());
    assertEquals("Place In The Sun", firstAlbum.getName());
    assertEquals(ObjectType.ALBUM, firstAlbum.getType());
    assertEquals("spotify:album:6akEvsycLGftJxYudPjmqK", firstAlbum.getUri());
    assertNotNull(firstAlbum.getAvailableMarkets());
    assertFalse(firstAlbum.getAvailableMarkets().isEmpty());
  }

}