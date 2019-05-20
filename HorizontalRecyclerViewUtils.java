package com.iberdrola.clientes.presentation.views.customviews.horizontalrecyclerview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.iberdrola.clientes.R;
import com.iberdrola.clientes.presentation.views.consumos.grafica.ConsumoGraficaRecyclerViewAdapter;
import com.iberdrola.clientes.presentation.views.consumos.grafica.CustomLinearSnapHelper;
import com.iberdrola.clientes.presentation.views.consumos.grafica.NoBounceLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import static com.iberdrola.clientes.presentation.converters.convertPixelsDp.convertDIPToPixels;

public class HorizontalRecyclerViewUtils implements GraficaRecyclerViewAdapter.ItemClickListener{

    private static TextView lastTextViewSelected = null; //Con más tiempo se podría hacer genérico
    private static int selectedPosition = -1;

    private RecyclerView recyclerView = null;
    private Activity activity = null;
    private Context context = null;
    private GraficaRecyclerViewAdapter adapter;

    public HorizontalRecyclerViewUtils(RecyclerView recyclerView, Activity activity, Context context)
    {
        this.recyclerView = recyclerView;
        this.activity = activity;
        this.context = context;

        //Cada vez que instancio borro para quitar datos pasados
        lastTextViewSelected = null;
        selectedPosition = -1;

        //Fuerzo el inicializar
        inicializarRecyclerView();
    }

    //region RecyclerView
    private void inicializarRecyclerView()
    {
        // set up the RecyclerView
        //recyclerView = getContext().findViewById(R.id.recyclerPeriodos);
        //Inicializo
        LinearLayoutManager horizontalLayoutManager
                = new NoBounceLinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);


        /*WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();*/

        //Configuro el padding
        Display display = activity.getWindowManager().getDefaultDisplay();
        int padding = display.getWidth()/2 - convertDIPToPixels(context, 40);
        recyclerView.setPadding(padding, 0, padding, 0);

        //region DATAMOCK
        List<String> temp = new ArrayList<String>();
        for(int i=2014; i<= 2019 ; ++i)
        {
            temp.add(""+ i);
        }
        //endregion

        //Introduzco los datos
        adapter = new GraficaRecyclerViewAdapter(context,temp/*fechas*/);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        //Escoger una fecha por defecto
        //lastTextViewSelected = ;

        //Añado snapping. Con el snap, encuentro el itemView que coge el propio snap.
        //Nota: Cambiar onViewState no implementado del customsnap
        LinearSnapHelper linearSnapHelper = new CustomLinearSnapHelper(recyclerView, selectedPosition );
        linearSnapHelper.attachToRecyclerView(recyclerView);

        //Preparo el onScroll para detectar objeto. Una vez salte el onScrolled, llamo al snap para obtener el item.
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() { //SACAR A UNA CLASE Y TERMINARLO
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    //Dragging
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    TextView tempTextView = getInstance(((CustomLinearSnapHelper) linearSnapHelper).findSnapView(horizontalLayoutManager));
                    if(inicializarLastTextViewSelected(tempTextView))
                    {
                        tempTextView.setTextAppearance(R.style.headers_h2);
                    }

                    if(comparacionLastTextViewSelected(tempTextView))
                    {
                        tempTextView.setTextAppearance(R.style.headers_h2);
                    }

                }
            }
        });

    }

    @SuppressLint("NewApi")
    @Override
    public void onItemClick(View view, int position) {
        TextView tempTextView = null;

        //No tengo claro si recibe el LinearLayout o el TextView por eso le pongo ambos.
        tempTextView = getInstance(view);


        //Evitaremos que al hacer click en el mismo objeto, haga cosas

        //Selecciono por primera vez
        if(inicializarLastTextViewSelected(tempTextView))
        {
            smoothScrollAnimation(lastTextViewSelected, position);
        }


        //lastTextView le reestablecemos el estilo, cambiamos al nuevo, y estilo nuevo.
        if(comparacionLastTextViewSelected(tempTextView))
        {
            smoothScrollAnimation(lastTextViewSelected, position);
        }

    }

    private TextView getInstance(View view)
    {
        TextView tempTextView = null;
        if(view instanceof LinearLayout)
        {
            tempTextView = (TextView)((LinearLayout) view).getChildAt( ((LinearLayout) view).getChildCount()-1);
        }else
        {
            tempTextView = (TextView) view;
        }
        return tempTextView;
    }

    private boolean inicializarLastTextViewSelected(TextView textView)
    {
        boolean result = false;
        if(lastTextViewSelected==null) {
            lastTextViewSelected = textView;
            result = true;
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean comparacionLastTextViewSelected(TextView textView)
    {
        boolean result = false;

        if(!textView.getText().toString().equals(lastTextViewSelected.getText().toString())) {
            lastTextViewSelected.setTextAppearance(R.style.headers_h2_gray);
            lastTextViewSelected = textView;
            result = true;
        }

        return result;
    }

    @SuppressLint("NewApi")
    private void smoothScrollAnimation(TextView view, int position)
    {
        //Cuando se hace click en alguno, que haga un scroll
        recyclerView.smoothScrollToPosition(position);

        //Cambiar el texto a negrita
        view.setTextAppearance(R.style.headers_h2);
    }
    //endregion
}
