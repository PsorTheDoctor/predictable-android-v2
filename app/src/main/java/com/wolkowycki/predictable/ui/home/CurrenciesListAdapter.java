package com.wolkowycki.predictable.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.wolkowycki.predictable.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import static java.lang.String.valueOf;

public class CurrenciesListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listCurrencies;
    private List<Float> listPrices;
    private List<String> listDates;
    private HashMap<String, List<Float>> mapPrices;
    private HashMap<String, List<Float>> mapChanges;

    public CurrenciesListAdapter(Context context,
                                 List<String> listCurrencies,
                                 List<Float> listPrices,
                                 List<String> listDates,
                                 HashMap<String, List<Float>> mapPrices,
                                 HashMap<String, List<Float>> mapChanges) {
        this.context = context;
        this.listCurrencies = listCurrencies;
        this.listPrices = listPrices;
        this.listDates = listDates;
        this.mapPrices = mapPrices;
        this.mapChanges = mapChanges;
    }

    @Override
    public int getGroupCount() {
        return listCurrencies.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mapPrices.get(listCurrencies.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listCurrencies.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mapPrices.get(listCurrencies.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.price_group, null);
        }

        final ImageView imgView = (ImageView) view.findViewById(R.id.currency_img);
        final TextView currencyView = (TextView) view.findViewById(R.id.currency);
        final TextView priceView = (TextView) view.findViewById(R.id.fresh_price);

        switch (headerTitle) {
            case "Bitcoin": imgView.setImageResource(R.drawable.bitcoin); break;
            case "Ethereum": imgView.setImageResource(R.drawable.ethereum); break;
            case "Ripple": imgView.setImageResource(R.drawable.ripple); break;
            case "Tether": imgView.setImageResource(R.drawable.tether); break;
            case "Chainlink": imgView.setImageResource(R.drawable.chainlink); break;
            case "Bitcoin Cash": imgView.setImageResource(R.drawable.bitcoin_cash); break;
            case "Cardano": imgView.setImageResource(R.drawable.cardano); break;
            case "Litecoin": imgView.setImageResource(R.drawable.litecoin); break;
            case "Bitcoin SV": imgView.setImageResource(R.drawable.bitcoin_sv); break;
            case "EOS": imgView.setImageResource(R.drawable.eos); break;
            case "Binance Coin": imgView.setImageResource(R.drawable.binance_coin); break;
            case "Crypto.com Coin": imgView.setImageResource(R.drawable.crypto_coin); break;
            case "Tezos": imgView.setImageResource(R.drawable.tezos); break;
            case "Stellar": imgView.setImageResource(R.drawable.stellar); break;
            case "TRON": imgView.setImageResource(R.drawable.tron); break;
            case "OKB": imgView.setImageResource(R.drawable.okb); break;
            case "Monero": imgView.setImageResource(R.drawable.monero); break;
            case "Cosmos": imgView.setImageResource(R.drawable.cosmos); break;
            case "VeChain": imgView.setImageResource(R.drawable.vechain); break;
            case "LEO Token": imgView.setImageResource(R.drawable.leo_token); break;
            default: break;
        }
        currencyView.setText(headerTitle);

        String txt = listPrices.get(groupPosition) + " $";
        priceView.setText(txt);

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {

        // final float childText = (float) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.price_item, null);
        }

        TableRow priceItem = (TableRow) view.findViewById(R.id.price_item);
        TextView dayView = (TextView) view.findViewById(R.id.day);
        TextView priceView = (TextView) view.findViewById(R.id.price);
        TextView changeView = (TextView) view.findViewById(R.id.change);

        String currency = (String) getGroup(groupPosition);
        List<Float> weekPrices = mapPrices.get(currency);
        List<Float> weekChanges = mapChanges.get(currency);

        if (!isLastChild) {
            // Show last row if hidden
            priceItem.setVisibility(View.VISIBLE);

            // Set date shortcut
            dayView.setText(listDates.get(childPosition));

            // Set price
            String price = valueOf(weekPrices.get(childPosition));
            priceView.setText(price);

            // Set price change
            float changeValue = weekChanges.get(childPosition);
            String change = changeValue + " %";
            if (changeValue < 0) {
                changeView.setText(change);
                changeView.setTextColor(Color.RED);
            } else {
                change = "+" + change;
                changeView.setText(change);
                changeView.setTextColor(Color.parseColor("#009933"));
            }

            // Bold font if date is today's
            Typeface normal = ResourcesCompat.getFont(context, R.font.century_gothic);
            Typeface bold = ResourcesCompat.getFont(context, R.font.century_gothic_bold);

            if (childPosition == 3) {
                dayView.setTypeface(bold);
                priceView.setTypeface(bold);
                changeView.setTypeface(bold);
            } else {
                dayView.setTypeface(normal);
                priceView.setTypeface(normal);
                changeView.setTypeface(normal);
            }

            // Set tan background for rows with predicted prices
            if (childPosition > 3) {
                priceItem.setBackgroundColor(Color.parseColor("#FFFDE7"));
            } else {
                priceItem.setBackgroundColor(Color.WHITE);
            }

            // Hide a chart
            LineChart chart = (LineChart) view.findViewById(R.id.chart);
            chart.setVisibility(View.GONE);
        } else {
            // Hide last not-so-meaning price
            priceItem.setVisibility(View.GONE);

            LineChart chart = (LineChart) view.findViewById(R.id.chart);
            chart.setVisibility(View.VISIBLE);
            LineDataSet lineDataSet = new LineDataSet(chartValues(weekPrices), "Price history");

            int[] colors = {
                    Color.parseColor("#00AAFF"),
                    Color.parseColor("#1AB2FF"),
                    Color.parseColor("#33BBFF"),
                    Color.parseColor("#4DC3FF"),
                    Color.parseColor("#66CCFF"),
                    Color.parseColor("#80D4FF"),
                    Color.parseColor("#99DDFF")
            };

            lineDataSet.setLineWidth(5);
            lineDataSet.setColors(colors);
            lineDataSet.setCircleColors(colors);
            lineDataSet.setDrawCircles(true);
            lineDataSet.setDrawCircleHole(false);
            // lineDataSet.setFillAlpha(200);
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillDrawable(ContextCompat.getDrawable(context, R.drawable.gradient_chart));

            // Set date labels
            XAxis xAxis = chart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(listDates));
            xAxis.setGranularity(1f);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            // Hide grid lines
            chart.getAxisLeft().setDrawGridLines(false);
            chart.getAxisRight().setDrawGridLines(false);
            xAxis.setDrawGridLines(false);

            // Hide a legend
            chart.setDescription(null);
            chart.getAxisRight().setDrawLabels(false);
            chart.getLegend().setEnabled(false);

            // Disable zoom
            chart.setScaleEnabled(false);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(lineDataSet);

            LineData data = new LineData(dataSets);
            chart.setData(data);
            chart.invalidate();
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private ArrayList<Entry> chartValues(List<Float> weekPrices) {
        ArrayList<Entry> values = new ArrayList<Entry>();

        for (int i = 0; i < weekPrices.size() - 1; i++) {
            values.add(new Entry(i, weekPrices.get(i)));
        }
        return values;
    }
}
