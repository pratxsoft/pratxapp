package br.com.blbbb.blbb.comparators;

import java.util.Comparator;

import br.com.blbbb.blbb.model.ItemDoPedido;

public class ItemPedidoComparatorPorNome implements Comparator<ItemDoPedido> {

    @Override
    public int compare(ItemDoPedido itemDoPedido, ItemDoPedido t1) {
        return itemDoPedido.getProduto().getNome().compareTo(t1.getProduto().getNome());
    }
}
