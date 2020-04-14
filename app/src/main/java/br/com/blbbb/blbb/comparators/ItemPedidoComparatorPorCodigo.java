package br.com.blbbb.blbb.comparators;

import java.util.Comparator;

import br.com.blbbb.blbb.model.ItemDoPedido;

public class ItemPedidoComparatorPorCodigo implements Comparator<ItemDoPedido> {

    @Override
    public int compare(ItemDoPedido itemDoPedido, ItemDoPedido t1) {
        return Long.valueOf(itemDoPedido.getProduto().getCodigo()).compareTo(Long.valueOf(t1.getProduto().getCodigo()));
    }
}
