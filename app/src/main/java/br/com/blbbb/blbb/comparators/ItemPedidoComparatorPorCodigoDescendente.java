package br.com.blbbb.blbb.comparators;

import java.util.Comparator;

import br.com.blbbb.blbb.model.ItemDoPedido;

public class ItemPedidoComparatorPorCodigoDescendente implements Comparator<ItemDoPedido> {

    @Override
    public int compare(ItemDoPedido itemDoPedido, ItemDoPedido t1) {
        return Long.valueOf(t1.getProduto().getCodigo()).compareTo(Long.valueOf(itemDoPedido.getProduto().getCodigo()));
    }
}
