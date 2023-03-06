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
	 * 1. Este m�todo objetiva guardar todos ItemVenda, mas assume que os objetos da classe {ItemVenda} seriam obtidos ou criados pelo 
	 * pr�prios c�digos clientes. Assumamos que objetos {ItemVenda} possuem o seu ciclo de vida (de sua instancia��o a sua destrui��o) 
	 * gerido ou determinado por um objeto da classe {Venda}.  Tamb�m sabemos que um objeto {ItemVenda} depende de uma associa��o
	 * com um objeto {Produto} para fazer sentido, indicando-lhe uma respectiva quantidade no contexto da {Venda} da qual  vier a fazer parte.
	 * 
	 * 2.  Considerando o contexto supracitado, poder�amos ficar em d�vida sobre deixar a assinatura e implementa��o deste m�todo
	 * tal como est� ou se dever�amos modific�-lo para que c�digos clientes deste m�todo fiquem mais respons�veis por ter que 
	 * criar e configurar objetos {ItemVenda} no processo de adicion�-los a um objeto {Venda}.  
	 * 
	 * ---------- 
	 * [PERGUNTA A] 
	 * Poder�amos aderir a qual princ�pio de atribui��o de responsabilidade a objetos (GRASP) aqui e por qu�? 
	 * Relate as vantagens de sua escolha, considerando controle de acoplamento, coes�o e reuso.
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
	 * 1. Este m�todo  objetiva calcular o subtotal de uma {Venda}, que � a quantidade do produto multiplicada pelo pre�o do mesmo.
	 * Essas informa��es est�o dispon�veis no objeto {ItemVenda}.  Veja que esse c�digo � do tipo uma �nica linha, muito simples, 
	 * apenas aplicando uma multiplica��o sobre os valores.
	 * 
	 * 2. Apesar de simples, o c�digo atual � for�ado a acessar um objeto da classe {Produto} para obter-lhes o pre�o. Com isso,
	 * percebemos que {Venda.getSubtotal()} gera um foco de acoplamento adicional com essa classe e poder�amos nos questionar
	 * se haveria uma forma de evitarmos isso. Ser� que {Venda} deve se acoplar assim com {Produto} para ser respons�vel por calcular
	 * um subtotal de um {ItemVenda} com o qual j� est� associada?  Este m�todo deveria ficar realmente aqui, sendo uma {Venda} apta
	 * em termos de acesso a informa��es para prover tal servi�o?
	 *   
	 * ---------- 
	 * [PERGUNTA B] 
	 * Poder�amos aderir a qual princ�pio de atribui��o de responsabilidade a objetos (GRASP) aqui e por qu�? 
	 * Relate as vantagens de sua escolha, considerando controle de acoplamento, coes�o e reuso.
	 * [COLOQUE A RESPOSTA AQUI]
	 * ----------
	 */
	private float getSubtotal(ItemVenda itemVenda) {
		return itemVenda.getProduto().getPreco() * itemVenda.getQuantidade();
	}

	/*
	 * TODO GRASP.03
	 * 1. O objetivo de um objeto da classe {Venda} � o de ABSTRTAIR o conjunto de informa��es sobre uma compra realizada, reunindo dados
	 * sobre produtos e quantidades adquiridas em mem�ria. Com esse entendimento, assumamos que qualquer c�digo
	 * e m�todos sobre {Venda} deve focar apenas em garantir o acesso e a validade desses dados, em mem�ria.  
	 * 
	 * 2. Este m�todo possui c�digo que permite elaborar uma nota fiscal com base na {Venda} em texto e envi�-lo por e-mail 
	 * e resolvemos num primeiro momento responsabilizar a classe {Venda} por prov�-lo, j� que � "especialista na informa��o" por
	 * possuir o acesso a todos os dados necess�rios a implementa��o disso e para que esse c�digo fique reus�vel (invoc�vel) por aqueles 
	 * c�digos clientes que desejarem isso. 
	 *   
	 * 3.  Entretanto, percebemos que esse c�digo acaba assumindo a responsabilidade question�vel de acessar
	 * objetos dda API JavaMail e para piorar, nem sempre c�digos clientes atuais ou futuros de objetos {Venda} podem querer emitir 
	 * nota fiscal sobre uma {Venda} ou poderiam se interssar sobre outras formas de gera��o ou envio. Seria plaus�vel que esta 
	 * classe {Venda} permanecesse coesa perante todos os seus  c�digos clientes atuais ou futuros assumindo a responsabilidade de 
	 * "enviar e-mail", ainda que os dados sejam dela mesma? Ser� que qualquer c�digo baseado em dados de {Venda} deva realmente 
	 * ficar nela para oportunizar que c�digos clientes possam convenientemente reutiliz�-lo?  Para se refletir esse dilema, partamos
	 * de um outro exemplo similar: seria plaus�vel que devido a um c�digo cliente precisar representar um objeto {Venda} em tela 
	 * usando Java Swing devamos prover m�todos para na pr�pria {Venda} para a conveni�ncia desse cliente, embora muitos outros 
	 * n�o vejam a {Venda} como �til para isso? 
	 * 
	 * ---------- 
	 * [PERGUNTA C] 
	 * Poder�amos aderir a qual princ�pio de atribui��o de responsabilidade a objetos (GRASP) aqui e por qu�? 
	 * Relate as vantagens de sua escolha, considerando controle de acoplamento, coes�o e reuso.
	 * [COLOQUE A RESPOSTA AQUI]
	 * ----------
	 */
	public boolean enviarNotaFiscalPorEmail() {
		String assunto = "VendaJava: " + this.getData();
		String mensagem = "Valor: " + this.getTotal();

		// Coloque seu e-mail@academico.ifpb.edu.br e senha aqui para testar. Depois de testado, apague-os ao devolver o c�digo ao entregar a atividade. 
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


