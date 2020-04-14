package br.com.blbbb.blbb.comparators;

import java.util.Comparator;

import br.com.blbbb.blbb.model.ItemDoPedido;

public class ItemPedidoComparatorPorNomeDescendente implements Comparator<ItemDoPedido> {

    @Override
    public int compare(ItemDoPedido itemDoPedido, ItemDoPedido t1) {
        return t1.getProduto().getNome().compareTo(itemDoPedido.getProduto().getNome());
    }
}
