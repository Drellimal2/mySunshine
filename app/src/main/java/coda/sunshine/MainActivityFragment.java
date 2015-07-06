package coda.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public class WeatherItem{
        public String day;
        public String forecast;

        public WeatherItem(String day, String forecast){
            this.day = day;
            this.forecast = forecast;
        }

    }

    ArrayList<WeatherItem> forecasts = new ArrayList<>();

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.forecastfragment, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_refresh) {
            updateWeather();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void updateWeather(){
        FetchWeatherTask fetchWeatherTask = new FetchWeatherTask(getActivity());
        String location = Utility.getPrefLocation(getActivity());
        Toast.makeText(getActivity(), ""+ location, Toast.LENGTH_LONG).show();

        fetchWeatherTask.execute(location);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        forecasts.add(new WeatherItem("Today", "Sunny"));
        forecasts.add(new WeatherItem("Tomorrow", "Rainy"));
        forecasts.add(new WeatherItem("The Next Day", "Sunny"));
        forecasts.add(new WeatherItem("The day after that", "Rainy"));
        forecasts.add(new WeatherItem("The day after that...", "Sunny"));
        forecasts.add(new WeatherItem("The day after that......", "Rainy"));
        forecasts.add(new WeatherItem("The day after that.........", "Sunny"));
        forecasts.add(new WeatherItem("The day after that............", "Rainy"));
        forecasts.add(new WeatherItem("The day after that", "Sunny"));
        forecasts.add(new WeatherItem("The day after that", "Rainy"));

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.main_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(false);

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }
        });

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(forecasts);
        mRecyclerView.setAdapter(mAdapter);


        return rootView;
    }


    @Override
    public void onStart(){
        updateWeather();
        super.onStart();
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private ArrayList<WeatherItem> mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView mDayView;
            public TextView mForecastView;

            public ViewHolder(View v) {
                super(v);
                mDayView = (TextView) v.findViewById(R.id.forecast_list_item_day);
                mForecastView = (TextView) v.findViewById(R.id.forecast_list_item_weather);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(ArrayList<WeatherItem> myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_forecast, parent, false);
            final ViewHolder vh = new ViewHolder(v);
            // set the view's size, margins, paddings and layout parameters
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemPosition = mRecyclerView.getChildPosition(v);
                    Toast.makeText(getActivity(), ""+itemPosition, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("FORECAST",forecasts.get(itemPosition).day
                            +"|||"+ forecasts.get(itemPosition).forecast);
                    startActivity(intent);

               }
            });


            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element

            holder.mDayView.setText(mDataset.get(position).day);
            holder.mForecastView.setText(mDataset.get(position).forecast);

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }
}
