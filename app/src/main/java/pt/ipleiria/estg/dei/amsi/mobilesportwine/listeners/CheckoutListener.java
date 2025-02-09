package pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners;

public interface CheckoutListener {
    // Agora o método recebe o invoiceId, orderId, totalAmount e status
    void onPagamentoSucesso(int invoiceId, int orderId, double totalAmount, String status);
    void onErroPagamento(String erro);

    // Se ainda usar o onFinalizarCompraSucesso, você pode deixar ou removê-lo,
    // dependendo do seu fluxo. Por exemplo:
    void onFinalizarCompraSucesso(int invoiceId, double totalPrice);
    void onErroFinalizarCompra(String erro);
}
