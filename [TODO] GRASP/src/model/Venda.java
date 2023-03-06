package model;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Venda {

	private Date data;

	private List<ItemVenda> itensVenda = new ArrayList<>();

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	/*
	 * TODO GRASP.01
	 * 1. Este método objetiva guardar todos ItemVenda, mas assume que os objetos da classe {ItemVenda} seriam obtidos ou criados pelo 
	 * próprios códigos clientes. Assumamos que objetos {ItemVenda} possuem o seu ciclo de vida (de sua instanciação a sua destruição) 
	 * gerido ou determinado por um objeto da classe {Venda}.  Também sabemos que um objeto {ItemVenda} depende de uma associação
	 * com um objeto {Produto} para fazer sentido, indicando-lhe uma respectiva quantidade no contexto da {Venda} da qual  vier a fazer parte.
	 * 
	 * 2.  Considerando o contexto supracitado, poderíamos ficar em dúvida sobre deixar a assinatura e implementação deste método
	 * tal como está ou se deveríamos modificá-lo para que códigos clientes deste método fiquem mais responsáveis por ter que 
	 * criar e configurar objetos {ItemVenda} no processo de adicioná-los a um objeto {Venda}.  
	 * 
	 * ---------- 
	 * [PERGUNTA A] 
	 * Poderíamos aderir a qual princípio de atribuição de responsabilidade a objetos (GRASP) aqui e por quê? 
	 * Relate as vantagens de sua escolha, considerando controle de acoplamento, coesão e reuso.
	 * [COLOQUE A RESPOSTA AQUI]
	 * ----------
	 */
	public void addItemVenda(ItemVenda itemVenda) {
		itensVenda.add(itemVenda);		
	}

	public float getTotal() {
		float total = 0;
		for (ItemVenda itemVenda : this.itensVenda) {
			total += getSubtotal(itemVenda);
		}
		return total;
	}

	/*
	 * TODO GRASP.02
	 * 1. Este método  objetiva calcular o subtotal de uma {Venda}, que é a quantidade do produto multiplicada pelo preço do mesmo.
	 * Essas informações estão disponíveis no objeto {ItemVenda}.  Veja que esse código é do tipo uma única linha, muito simples, 
	 * apenas aplicando uma multiplicação sobre os valores.
	 * 
	 * 2. Apesar de simples, o código atual é forçado a acessar um objeto da classe {Produto} para obter-lhes o preço. Com isso,
	 * percebemos que {Venda.getSubtotal()} gera um foco de acoplamento adicional com essa classe e poderíamos nos questionar
	 * se haveria uma forma de evitarmos isso. Será que {Venda} deve se acoplar assim com {Produto} para ser responsável por calcular
	 * um subtotal de um {ItemVenda} com o qual já está associada?  Este método deveria ficar realmente aqui, sendo uma {Venda} apta
	 * em termos de acesso a informações para prover tal serviço?
	 *   
	 * ---------- 
	 * [PERGUNTA B] 
	 * Poderíamos aderir a qual princípio de atribuição de responsabilidade a objetos (GRASP) aqui e por quê? 
	 * Relate as vantagens de sua escolha, considerando controle de acoplamento, coesão e reuso.
	 * [COLOQUE A RESPOSTA AQUI]
	 * ----------
	 */
	private float getSubtotal(ItemVenda itemVenda) {
		return itemVenda.getProduto().getPreco() * itemVenda.getQuantidade();
	}

	/*
	 * TODO GRASP.03
	 * 1. O objetivo de um objeto da classe {Venda} é o de ABSTRTAIR o conjunto de informações sobre uma compra realizada, reunindo dados
	 * sobre produtos e quantidades adquiridas em memória. Com esse entendimento, assumamos que qualquer código
	 * e métodos sobre {Venda} deve focar apenas em garantir o acesso e a validade desses dados, em memória.  
	 * 
	 * 2. Este método possui código que permite elaborar uma nota fiscal com base na {Venda} em texto e enviá-lo por e-mail 
	 * e resolvemos num primeiro momento responsabilizar a classe {Venda} por provê-lo, já que é "especialista na informação" por
	 * possuir o acesso a todos os dados necessários a implementação disso e para que esse código fique reusável (invocável) por aqueles 
	 * códigos clientes que desejarem isso. 
	 *   
	 * 3.  Entretanto, percebemos que esse código acaba assumindo a responsabilidade questionável de acessar
	 * objetos dda API JavaMail e para piorar, nem sempre códigos clientes atuais ou futuros de objetos {Venda} podem querer emitir 
	 * nota fiscal sobre uma {Venda} ou poderiam se interssar sobre outras formas de geração ou envio. Seria plausível que esta 
	 * classe {Venda} permanecesse coesa perante todos os seus  códigos clientes atuais ou futuros assumindo a responsabilidade de 
	 * "enviar e-mail", ainda que os dados sejam dela mesma? Será que qualquer código baseado em dados de {Venda} deva realmente 
	 * ficar nela para oportunizar que códigos clientes possam convenientemente reutilizá-lo?  Para se refletir esse dilema, partamos
	 * de um outro exemplo similar: seria plausível que devido a um código cliente precisar representar um objeto {Venda} em tela 
	 * usando Java Swing devamos prover métodos para na própria {Venda} para a conveniência desse cliente, embora muitos outros 
	 * não vejam a {Venda} como útil para isso? 
	 * 
	 * ---------- 
	 * [PERGUNTA C] 
	 * Poderíamos aderir a qual princípio de atribuição de responsabilidade a objetos (GRASP) aqui e por quê? 
	 * Relate as vantagens de sua escolha, considerando controle de acoplamento, coesão e reuso.
	 * [COLOQUE A RESPOSTA AQUI]
	 * ----------
	 */
	public boolean enviarNotaFiscalPorEmail() {
		String assunto = "VendaJava: " + this.getData();
		String mensagem = "Valor: " + this.getTotal();

		// Coloque seu e-mail@academico.ifpb.edu.br e senha aqui para testar. Depois de testado, apague-os ao devolver o código ao entregar a atividade. 
		String email = "";
		String senha = "";

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(email, senha);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("from@vendajava.pp.ads"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(email));
			message.setSubject(assunto);
			message.setText(mensagem);

			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			return false;
		}
	}
}


