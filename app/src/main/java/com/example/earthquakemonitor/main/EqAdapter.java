package com.example.earthquakemonitor.main;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.earthquakemonitor.Earthquake;
import com.example.earthquakemonitor.databinding.EqListItemBinding;

public class EqAdapter extends ListAdapter<Earthquake, EqAdapter.EqViewHolder> {

    public static final DiffUtil.ItemCallback<Earthquake> DIIF_CALLBACK =
            new DiffUtil.ItemCallback<Earthquake>() {
                @Override
                public boolean areItemsTheSame(@NonNull Earthquake oldEarthquake, @NonNull Earthquake newEarthquake) {
                    return oldEarthquake.getId().equals(newEarthquake.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Earthquake oldEarthquake, @NonNull Earthquake newEarthquake) {
                    return oldEarthquake.equals(newEarthquake);
                }
            };

    protected EqAdapter() {
        super(DIIF_CALLBACK);
    }

    private OnItemClickListener onItemClickLister;

    interface OnItemClickListener {
        void onItemClick(Earthquake earthquake);
    } //Se crea un Interface y dentro de ella un metodo

    public void setOnItemClickLister(OnItemClickListener onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    } //Setter de la interface

    //onCreateViewHolder y onBindViewHolder se ejecutan por cada uno de los alementos de la lista
    //Si tenemos 5 terremotos el onCreate.. se ejecuta 5 veces
    //El onBind.. es el que se encarga de decirle al ViewHolder cual tiene que pintar
    @NonNull
    @Override
    public EqViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //El parent es el RecyclerView
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.eq_list_item, parent, false);
        EqListItemBinding binding  = EqListItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        //return new EqViewHolder(view);
        return new EqViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EqViewHolder holder, int position) {
         Earthquake earthquake = getItem(position);

         holder.bind(earthquake);
    }

    class EqViewHolder extends RecyclerView.ViewHolder {

        //private TextView magnitudeText;
        //private TextView placeText;

        private final EqListItemBinding binding;

        /*public EqViewHolder(@NonNull View itemView) {
            super(itemView); //ItemView es el layout completo

             magnitudeText = itemView.findViewById(R.id.magnitude_text);
            placeText = itemView.findViewById(R.id.place_text);
        }*/

        public EqViewHolder(@NonNull EqListItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public void bind(Earthquake earthquake) {

            //magnitudeText.setText(String.valueOf(earthquake.getMagnitude()));
            //placeText.setText(earthquake.getPlace());
            binding.magnitudeText.setText(String.valueOf(earthquake.getMagnitude()));
            binding.placeText.setText(earthquake.getPlace());

            binding.getRoot().setOnClickListener( v -> {
                onItemClickLister.onItemClick(earthquake);
            }); //Se coloca este setOnClickListener para cualquier punto del layout eq_list_item
                //Se utiliza para dar click a cualquier punto de la pantalla

            binding.executePendingBindings(); //Nos permite que cuando demos scroll al RecylcerView
                                              //hace que esto se pinte de manera inmediata
        }

    }
}
