package ahmed.example.com.bakingapp.Widget;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import ahmed.example.com.bakingapp.R;
import ahmed.example.com.bakingapp.Recipes.RecipesActivity;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId
    ) {
        Intent intent = new Intent(context,
                                   RecipesActivity.class
        );
        String widgetText = context.getSharedPreferences(RecipesActivity.SHARED_NAME,
                                                         Activity.MODE_PRIVATE
        ).getString(RecipesActivity.RECIPE_NAME, "");
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.container, pendingIntent);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        Intent widgetServiceIntent=new Intent(context, WidgetServiceIntent.class);
        views.setRemoteAdapter(R.id.widget_list,widgetServiceIntent);
        views.setEmptyView(R.id.widget_list,R.id.empty_view);



        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

