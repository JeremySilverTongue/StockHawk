package com.sam_chordas.android.stockhawk.data;

import android.net.Uri;
import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by sam_chordas on 10/5/15.
 */
@ContentProvider(authority = QuoteProvider.AUTHORITY, database = QuoteDatabase.class)
public class QuoteProvider {
  public static final String AUTHORITY = "com.sam_chordas.android.stockhawk.data.QuoteProvider";

  static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

  interface Path{
    String QUOTES = "quotes";
    String HISTORICAL_DATA_PATH = "historical_data";
  }

  private static Uri buildUri(String... paths){
    Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
    for (String path:paths){
      builder.appendPath(path);
    }
    return builder.build();
  }

  @TableEndpoint(table = QuoteDatabase.QUOTES)
  public static class Quotes{
    @ContentUri(
        path = Path.QUOTES,
        type = "vnd.android.cursor.dir/quote"
    )
    public static final Uri CONTENT_URI = buildUri(Path.QUOTES);

    @InexactContentUri(
        name = "QUOTE_ID",
        path = Path.QUOTES + "/*",
        type = "vnd.android.cursor.item/quote",
        whereColumn = QuoteColumns.SYMBOL,
        pathSegment = 1
    )
    public static Uri withSymbol(String symbol){
      return buildUri(Path.QUOTES, symbol);
    }
  }

  @TableEndpoint(table = QuoteDatabase.HISTORICAL_DATA)
  public static class Historical_Data{

    @InexactContentUri(
            name = "HISTORICAL_DATA_SYMBOL",
            path = Path.HISTORICAL_DATA_PATH + "/*",
            type = "vnd.android.cursor.item/historical_data",
            whereColumn = HistoricalQuoteColumns.COLUMN_SYMBOL,
            pathSegment = 1,
            defaultSort = HistoricalQuoteColumns.COLUMN_DATE + " ASC"
    )
    public static Uri historicalDataPathWith(String symbol){
      return buildUri(Path.HISTORICAL_DATA_PATH, symbol);
    }
  }

}
