package br.com.blbbb.blbb.adapter.list;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.blbbb.blbb.R;
import br.com.blbbb.blbb.model.ItemDoPedido;

/**
 * Created by bruno on 31/03/2015.
 */
public class ItensDoPedidoAdapter extends ArrayAdapter<ItemDoPedido> {

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     */
    public ItensDoPedidoAdapter(Context context, int resource, List<ItemDoPedido> itens) {
        super(context, resource, itens);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // se nao for nulla, cria o layoute da view
        if (convertView == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());

            // usa o list_view_cliente.xml como layout pra view
            convertView = vi.inflate(R.layout.list_view_itens_pedido, null);
        }

        // usa o metodo getItem do adpater para pegar
        // o item do pedido na posicao do parametro position
        final ItemDoPedido itemDoPedido = getItem(position);

        if(itemDoPedido != null) {
            // cria as textViews (e outros gui components (arquivo list_view_cardapio.xml) para exibir os dados de um produto

            //o campo estoque do produto e usado aqui como quantidade do item do pedido. por isso sera zerado

            final TextView tvCardapioProdutoNome = (TextView) convertView.findViewById(R.id.tvCardapioProdutoNome);
            final TextView tvCardapioStatus = (TextView) convertView.findViewById(R.id.tvCardapioStatus);
            final TextView tvCardapioProdutoQuantidade = (TextView) convertView.findViewById(R.id.tvCardapioProdutoQuantidade);
            final TextView tvCardapioProdutoObservacao = (TextView) convertView.findViewById(R.id.tvCardapioProdutoOBS);

            if (tvCardapioProdutoNome != null) {
                tvCardapioProdutoNome.setText(itemDoPedido.getProduto().getNome());
            }
            if (tvCardapioStatus != null) {
                tvCardapioStatus.setText(String.valueOf(itemDoPedido.getStatus()));
            }
            if (tvCardapioProdutoQuantidade != null) {
                tvCardapioProdutoQuantidade.setText(String.valueOf(itemDoPedido.getQuantidade()));
            }
            if (tvCardapioProdutoObservacao != null) {
                if(itemDoPedido.getObservacao().isEmpty()){
                    itemDoPedido.setObservacao("");
                }
                tvCardapioProdutoObservacao.setText(String.valueOf(itemDoPedido.getObservacao()));
            }

        }
        return convertView;
    }

}
