package br.com.blbbb.blbb.adapter.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.blbbb.blbb.R;
import br.com.blbbb.blbb.model.ItemDoPedido;

/**
 * Created by bruno on 31/03/2015.
 */
public class CardapioAdapter extends ArrayAdapter<ItemDoPedido> {


    private List<ItemDoPedido> itemDoPedidoList;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     */
    public CardapioAdapter(Context context, int resource, List<ItemDoPedido> itens) {
        super(context, resource, itens);
        this.itemDoPedidoList = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // se nao for nulla, cria o layoute da view
        if (convertView == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());

            // usa o list_view_cliente.xml como layout pra view
            convertView = vi.inflate(R.layout.list_view_cardapio, null);
        }

        // usa o metodo getItem do adpater para pegar
        // o item do pedido na posicao do parametro position
        final ItemDoPedido itemDoPedido = getItem(position);

        if (itemDoPedido != null) {
            // cria as textViews (e outros gui components (arquivo list_view_cardapio.xml) para exibir os dados de um produto

            //o campo estoque do produto e usado aqui como quantidade do item do pedido. por isso sera zerado

            final TextView tvCardapioProdutoCodigo = (TextView) convertView.findViewById(R.id.tvCardapioProdutoCodigo);
            final TextView tvCardapioProdutoNome = (TextView) convertView.findViewById(R.id.tvCardapioProdutoNome);
            final TextView tvCardapioProdutoDescricao = (TextView) convertView.findViewById(R.id.tvCardapioProdutoDescricao);
            final TextView tvCardapioProdutoValor = (TextView) convertView.findViewById(R.id.tvCardapioProdutoValor);
            final TextView tvCardapioProdutoQuantidade = (TextView) convertView.findViewById(R.id.tvCardapioProdutoQuantidade);
            final TextView tvCardapioProdutoObservacao = (TextView) convertView.findViewById(R.id.tvCardapioProdutoOBS);

            if (tvCardapioProdutoCodigo != null) {
                tvCardapioProdutoCodigo.setText(String.valueOf(itemDoPedido.getProduto().getCodigo()));
            }
            if (tvCardapioProdutoNome != null) {
                tvCardapioProdutoNome.setText(itemDoPedido.getProduto().getNome());
            }
            if (tvCardapioProdutoDescricao != null) {
                tvCardapioProdutoDescricao.setText(itemDoPedido.getProduto().getDescricao());
            }
            if (tvCardapioProdutoValor != null) {
                tvCardapioProdutoValor.setText(String.valueOf(itemDoPedido.getProduto().getValor().getMoney()));
            }
            if (tvCardapioProdutoQuantidade != null) {
                tvCardapioProdutoQuantidade.setText(String.valueOf(itemDoPedido.getQuantidade()));
            }
            if (tvCardapioProdutoObservacao != null) {
                if (itemDoPedido.getObservacao() == null) {
                    itemDoPedido.setObservacao("");
                }
                tvCardapioProdutoObservacao.setText(String.valueOf(itemDoPedido.getObservacao()));
            }
        }
        return convertView;
    }

    public void addItemDoPedido(ItemDoPedido pItemDoPedido) {
        this.itemDoPedidoList.add(pItemDoPedido);
    }

    public void removerItemDoPedido(ItemDoPedido pItemDoPedido){
        this.itemDoPedidoList.remove(pItemDoPedido);
    }

    public boolean existeOItem(ItemDoPedido pItemDoPedido) {
        if (this.itemDoPedidoList.contains(pItemDoPedido)) {
            return true;
        }
        return false;
    }

    public List<ItemDoPedido> getItemDoPedidoList() {
        return itemDoPedidoList;
    }

}
