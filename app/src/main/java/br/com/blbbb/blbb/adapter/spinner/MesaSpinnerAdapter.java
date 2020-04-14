package br.com.blbbb.blbb.adapter.spinner;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.com.blbbb.blbb.model.Mesa;

public class MesaSpinnerAdapter extends ArrayAdapter<Mesa> {

	// contexto
	private Context context;

	// valores customizados para spinner (Mesa)
	private List<Mesa> listaMesa;

	/**
	 * Construtor
	 *
	 * @param context
	 * @param textViewResourceId
	 * @param pListaMesa
	 */
	public MesaSpinnerAdapter(Context context, int textViewResourceId, List<Mesa> pListaMesa) {
		super(context, textViewResourceId, pListaMesa);
		this.context = context;
		this.listaMesa = pListaMesa;
	}

	public int getCount() {
		return listaMesa.size();
	}

	public Mesa getMesa(int position) {
		return listaMesa.get(position);
	}

	public long getMesaId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// crio o textView
		TextView label = new TextView(context);
		label.setTextColor(Color.BLACK);

		// seto o texto do textView com os dados vindos da lista de mesas
		label.setText(listaMesa.get(position).getMeMesa());

		return label;
	}

    /**
     * dropdown do spinner ativado aqui
     */
    @Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		TextView label = new TextView(context);
		label.setTextColor(Color.BLACK);
		label.setPadding(5, 30, 5, 30);
		label.setText(listaMesa.get(position).toString());

		return label;
	}

}
