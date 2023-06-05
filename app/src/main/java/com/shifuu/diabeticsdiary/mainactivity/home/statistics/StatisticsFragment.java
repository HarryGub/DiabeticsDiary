package com.shifuu.diabeticsdiary.mainactivity.home.statistics;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PanZoom;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYSeries;
import com.shifuu.diabeticsdiary.R;
import com.shifuu.diabeticsdiary.database.food.FoodEntity;
import com.shifuu.diabeticsdiary.database.food_rec.FoodRecEntity;
import com.shifuu.diabeticsdiary.database.sugar_rec.SugarRecEntity;
import com.shifuu.diabeticsdiary.databinding.FragmentStatisticsBinding;
import com.shifuu.diabeticsdiary.util.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StatisticsFragment extends Fragment {

    private static final double millisInDay = 86400000.0;

    private StatisticsViewModel viewModel;
    private FragmentStatisticsBinding binding;
    private Map<LocalDate, Double> calPointMap;
    private Map<LocalDate, Double> xePointMap;
    private Pair<List<Long>, List<Double>> sugarPointMap;


    private final Format sugarLabelFormatter = new Format() {
        @Override
        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            // obj contains the raw Number value representing the position of the label being drawn.
            // customize the labeling however you want here:
            int i = Math.round(((Number) obj).floatValue());
            String s;
            try {
                s= Util
                        .dateFormatter
                        .format(LocalDateTime.ofEpochSecond(sugarPointMap.first.get(i), 0,ZoneOffset.UTC).toLocalDate()
                        ).substring(0, 5);
            }
            catch (Exception e)
            {
                s = " ";
            }
            return toAppendTo.append(s);
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            // unused
            return null;
        }
    };


    public static StatisticsFragment newInstance() {
        return new StatisticsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(StatisticsViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fragStatChangeParam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Observer<ChangeGraphDialogViewModel> observer = new Observer<ChangeGraphDialogViewModel>() {
                    @Override
                    public void onChanged(ChangeGraphDialogViewModel changeGraphDialogViewModel) {
                        viewModel.setStart(changeGraphDialogViewModel.getStart());
                        viewModel.setEnd(changeGraphDialogViewModel.getEnd());
                        viewModel.setGraphItemType(changeGraphDialogViewModel.getGraphItemType());

                        updateView();
                    }
                };

                ChangeGraphDialogFragment fragment = new ChangeGraphDialogFragment();
                fragment.showWithArgs(getChildFragmentManager(), "ChangeGraphParamsDialog", viewModel, observer);
            }
        });


        updateView();

    }

    private void updateView() {

        binding.fragStatNomenVal.setText(viewModel.getGraphItemType() == StatisticsViewModel.CALORIES_ID ? "Калории" : "Сахар");

        switch (viewModel.getGraphItemType()){
            case StatisticsViewModel.CALORIES_ID: binding.fragStatNomenVal.setText("Калории"); setCalGraph(); break;
            case StatisticsViewModel.SUGAR_ID: binding.fragStatNomenVal.setText("Сахар"); setSugGraph(); break;
            case StatisticsViewModel.XE_ID: binding.fragStatNomenVal.setText("Хлебные единицы"); setXEGraph(); break;
        }

        binding.fragStatPeriodStartVal.setText(Util.dateFormatter.format(viewModel.getStart()));
        binding.fragStatPeriodEndVal.setText(Util.dateFormatter.format(viewModel.getEnd()));
    }

    private void setXEGraph() {
        List<LiveData<List<FoodRecEntity>>> list = viewModel.getFoodRecsForCurrentViewModelPeriod();

        clearGraph();


        Log.d("StatFrag", "list Size: " + list.size());

        xePointMap = new ArrayMap<>();

        list.forEach(listLiveData -> {

            //For each day
            listLiveData.observe(getViewLifecycleOwner(), new Observer<List<FoodRecEntity>>() {
                @Override
                public void onChanged(List<FoodRecEntity> foodRecEntities) {

                    // For each food rec in day
                    foodRecEntities.forEach(foodRecEntity -> {
                        // Get food entity
                        viewModel.getFoodEntityById(foodRecEntity.getFoodId()).observe(getViewLifecycleOwner(), new Observer<FoodEntity>() {
                            @Override
                            public void onChanged(FoodEntity food) {



                                if (xePointMap.containsKey(foodRecEntity.getDate()))
                                    xePointMap.put(foodRecEntity.getDate(),
                                            xePointMap.get(foodRecEntity.getDate()) +
                                                    (food.getCarbs() * foodRecEntity.getMass()/10));
                                else
                                    xePointMap.put(foodRecEntity.getDate(),
                                            (double)(foodRecEntity.getMass() * food.getCarbs()/10));

                                updateXEGraph();
                            }
                        });
                    });
                }
            });
        });
    }



    private void setSugGraph()
    {
        List<LiveData<List<SugarRecEntity>>> list = viewModel.getSugarRecsForCurrentViewModelPeriod();


        clearGraph();


        Log.d("StatFrag", "list Size: " + list.size());

        sugarPointMap = new Pair<>(new ArrayList<>(), new ArrayList<>());

        list.forEach(listLiveData -> {
            //For eachDay

            listLiveData.observe(getViewLifecycleOwner(), new Observer<List<SugarRecEntity>>() {
                @Override
                public void onChanged(List<SugarRecEntity> sugarRecEntities) {
                    sugarRecEntities.forEach(new Consumer<SugarRecEntity>() {
                        @Override
                        public void accept(SugarRecEntity sugarRecEntity) {
                            Log.d("StatFrag", "point: "
                                            + sugarRecEntity.getTime().toEpochSecond(ZoneOffset.UTC)
                                            + " " + sugarRecEntity.getTime()
                                    + " " + sugarRecEntity.getValue());

                            sugarPointMap.first.add(sugarRecEntity.getTime().toEpochSecond(ZoneOffset.UTC));
                            sugarPointMap.second.add(sugarRecEntity.getValue());

                            updateSugarGraph();
                        }
                    });
                }
            });
        });
    }

    private void clearGraph() {
        binding.fragStatAvgVal.setText("Нет данных");
        binding.fragStatGraph.clear();
        binding.fragStatGraph.redraw();
    }

    private void setCalGraph()
    {
        List<LiveData<List<FoodRecEntity>>> list = viewModel.getFoodRecsForCurrentViewModelPeriod();

        clearGraph();


        Log.d("StatFrag", "list Size: " + list.size());

        calPointMap = new ArrayMap<>();

        list.forEach(listLiveData -> {

            //For each day
            listLiveData.observe(getViewLifecycleOwner(), new Observer<List<FoodRecEntity>>() {
                @Override
                public void onChanged(List<FoodRecEntity> foodRecEntities) {

                    // For each food rec in day
                    foodRecEntities.forEach(foodRecEntity -> {
                        // Get food entity
                        viewModel.getFoodEntityById(foodRecEntity.getFoodId()).observe(getViewLifecycleOwner(), new Observer<FoodEntity>() {
                            @Override
                            public void onChanged(FoodEntity food) {


                                if (calPointMap.containsKey(foodRecEntity.getDate()))
                                    calPointMap.put(foodRecEntity.getDate(),
                                            calPointMap.get(foodRecEntity.getDate()) +
                                                    Math.round(food.getKal() * foodRecEntity.getMass()));
                                else
                                    calPointMap.put(foodRecEntity.getDate(),
                                            (double)Math.round(foodRecEntity.getMass() * food.getKal()));

                                updateCalGraph();
                            }
                        });
                    });
                }
            });
        });
    }

    private void updateSugarGraph()
    {

        sugarPointMap.first.forEach(dataPoint -> {
            LocalDateTime ldt = LocalDateTime.ofEpochSecond(dataPoint, 0, ZoneOffset.UTC);
            Log.d("StatFrag", dataPoint + " " + ldt.toString());
        });
        Log.d("StatFrag", "                     ");

        sugarPointMap = sortPair(sugarPointMap);

        XYSeries series = new SimpleXYSeries(sugarPointMap.second, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, null);

        binding.fragStatGraph.clear();
        binding.fragStatGraph.addSeries(series, new LineAndPointFormatter(getContext(), R.xml.line_point_formatter_with_labels));
        binding.fragStatGraph.getGraph().setLineLabelEdges(
                XYGraphWidget.Edge.BOTTOM,
                XYGraphWidget.Edge.LEFT);
        binding.fragStatGraph.setDomainBoundaries(-1, sugarPointMap.first.size(), BoundaryMode.FIXED);
        binding.fragStatGraph.setRangeBoundaries(0, sugarPointMap.second.stream().max(Double::compareTo).get() + 3, BoundaryMode.FIXED);

        PanZoom.attach(binding.fragStatGraph);

        binding.fragStatGraph.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(sugarLabelFormatter);

        binding.fragStatGraph.redraw();


        double avg = sugarPointMap.second.stream().reduce((double) 0, Double::sum)/sugarPointMap.second.size();

        Log.d("StatisticsFrag", String.valueOf(avg));

        binding.fragStatAvgVal.setText(BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_DOWN) + " ммоль/л");
    }



    private void updateCalGraph()
    {

        Pair<List<LocalDate>, List<Double>> calPointList = mapToSortedPairOfLists(fillMapGaps(calPointMap));

        XYSeries series = new SimpleXYSeries(calPointList.second, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, null);

        binding.fragStatGraph.clear();
        binding.fragStatGraph.addSeries(series, new LineAndPointFormatter(getContext(), R.xml.line_point_formatter_with_labels));
        binding.fragStatGraph.getGraph().setLineLabelEdges(
                XYGraphWidget.Edge.BOTTOM,
                XYGraphWidget.Edge.LEFT);
        binding.fragStatGraph.setDomainBoundaries(-1, calPointList.first.size(), BoundaryMode.FIXED);
        binding.fragStatGraph.setRangeBoundaries(-200,
                calPointList.second.stream().max(Double::compareTo).get() + 200,
                BoundaryMode.FIXED);

        PanZoom.attach(binding.fragStatGraph);

        binding.fragStatGraph.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer stringBuffer, FieldPosition fieldPosition) {
                int i = Math.round(((Number) obj).floatValue());
                String s;
                try {
                    s= Util.dateFormatter.format(calPointList.first.get(i)).substring(0, 5);
                }
                catch (Exception e)
                {
                    s = " ";
                }
                return stringBuffer.append(s);
            }

            @Override
            public Object parseObject(String s, ParsePosition parsePosition) {
                return null;
            }
        });

        binding.fragStatGraph.redraw();


        double avg = calPointList.second.stream().reduce(0.0, Double::sum)/calPointList.second.size();

        Log.d("StatisticsFrag", String.valueOf(avg));

        binding.fragStatAvgVal.setText(Math.round(avg) + " ккал");

    }

    private void updateXEGraph() {

        Pair<List<LocalDate>, List<Double>> xePointList = mapToSortedPairOfLists(fillMapGaps(xePointMap));


        // xePointList.second.stream().map(aDouble -> Util.round(aDouble, 2)).collect(Collectors.toList());

        Log.d("StatisticsFrag", viewModel.getCurrentPeriodInDays() + " days");


        for (int i = 0; i < xePointList.second.size(); i++) {
            xePointList.second.set(i, Util.round(xePointList.second.get(i), 2));
        }

        XYSeries series = new SimpleXYSeries(xePointList.second, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, null);

        binding.fragStatGraph.clear();
        binding.fragStatGraph.addSeries(series, new LineAndPointFormatter(getContext(), R.xml.line_point_formatter_with_labels));
        binding.fragStatGraph.getGraph().setLineLabelEdges(
                XYGraphWidget.Edge.BOTTOM,
                XYGraphWidget.Edge.LEFT);
        binding.fragStatGraph.setDomainBoundaries(-1, xePointList.first.size(), BoundaryMode.FIXED);
        binding.fragStatGraph.setRangeBoundaries(-10,
                xePointList.second.stream().max(Double::compareTo).get() + 20,
                BoundaryMode.FIXED);

        PanZoom.attach(binding.fragStatGraph);

        binding.fragStatGraph.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer stringBuffer, FieldPosition fieldPosition) {
                int i = Math.round(((Number) obj).floatValue());
                String s;
                try {
                    s= Util.dateFormatter.format(xePointList.first.get(i)).substring(0, 5);
                }
                catch (Exception e)
                {
                    s = " ";
                }
                return stringBuffer.append(s);
            }

            @Override
            public Object parseObject(String s, ParsePosition parsePosition) {
                return null;
            }
        });

        binding.fragStatGraph.redraw();


        double avg = xePointList.second.stream().reduce(0.0, Double::sum)/xePointList.second.size();

        Log.d("StatisticsFrag", String.valueOf(avg));


        binding.fragStatAvgVal.setText(Util.round(avg, 2) + " ХЕ");
    }

    private Map<LocalDate, Double> fillMapGaps(Map<LocalDate, Double> map) {
        for (LocalDate i = viewModel.getStart(); i.isBefore(viewModel.getEnd()); i = i.plusDays(1)) {
            if (!map.containsKey(i))
                map.put(i, 0.0);
            Log.d("StatisticsFrag", map.get(i).toString() + " " + i);
        }
        return map;
    }


    private Pair<List<Long>, List<Double>> sortPair(Pair<List<Long>, List<Double>> sugarPointMap) {
        List<Pair<Long,Double>> tmp = new ArrayList<>();

        for (int i = 0; i < sugarPointMap.first.size(); i++) {
            tmp.add(new Pair<>(sugarPointMap.first.get(i), sugarPointMap.second.get(i)));
        }

        Comparator<Pair<Long, Double>> comparator = (dataPoint, t1) -> {
            if (dataPoint.first.equals(t1.first))
                return 0;
            return Long.compare(dataPoint.first, t1.first);
        };
        tmp.sort(comparator);

        sugarPointMap = new Pair<>(new ArrayList<>(), new ArrayList<>());

        for (int i = 0; i < tmp.size(); i++) {
            sugarPointMap.first.add(tmp.get(i).first);
            sugarPointMap.second.add(tmp.get(i).second);
        }
        return sugarPointMap;

    }




    private static Pair<List<LocalDate>, List<Double>> mapToSortedPairOfLists(Map<LocalDate, Double> map)
    {
        List<Pair<LocalDate, Double>> tmp = new ArrayList<>();
        Set<Map.Entry<LocalDate, Double>> set = map.entrySet();

        set.forEach(localDateDoubleEntry -> tmp.add(new Pair<>(localDateDoubleEntry.getKey(), localDateDoubleEntry.getValue())));

        tmp.sort(new Comparator<Pair<LocalDate, Double>>() {
            @Override
            public int compare(Pair<LocalDate, Double> localDateDoublePair, Pair<LocalDate, Double> t1) {
                return localDateDoublePair.first.compareTo(t1.first);
            }
        });

        Pair<List<LocalDate>,List<Double>> calPointPair = new Pair<>(new ArrayList<>(), new ArrayList<>());

        for (int i = 0; i < tmp.size(); i++) {
            calPointPair.first.add(tmp.get(i).first);
            calPointPair.second.add(tmp.get(i).second);
        }

        return calPointPair;
    }
}



















