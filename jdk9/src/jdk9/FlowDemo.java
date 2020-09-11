package jdk9;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

public class FlowDemo {

	public static void main(String[] args) throws Exception {
		// 1. ���巢����, ���������������� Integer
		// ֱ��ʹ��jdk�Դ���SubmissionPublisher, ��ʵ���� Publisher �ӿ�
		SubmissionPublisher<Integer> publiser = new SubmissionPublisher<Integer>();

		// 2. ���嶩����
		Subscriber<Integer> subscriber = new Subscriber<Integer>() {

			private Subscription subscription;

			@Override
			public void onSubscribe(Subscription subscription) {

				this.subscription = subscription;
				this.subscription.request(1);
			}

			@Override
			public void onNext(Integer item) {


				try {
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				// ���������request������һ������
				this.subscription.request(1);

				// ���� �Ѿ��ﵽ��Ŀ��, ����cancel���߷����߲��ٽ���������
				// this.subscription.cancel();
			}

			@Override
			public void onError(Throwable throwable) {
				// �������쳣(���紦�����ݵ�ʱ��������쳣)
				throwable.printStackTrace();

				// ���ǿ��Ը��߷�����, ���治����������
				this.subscription.cancel();
			}

			@Override
			public void onComplete() {
				// ȫ�����ݴ�������(�����߹ر���)
				System.out.println("��������!");
			}

		};

		// 3. �����ߺͶ����� �������Ĺ�ϵ
		publiser.subscribe(subscriber);

		// 4. ��������, ������
		// �������������������
		for (int i = 0; i < 1000; i++) {
			System.out.println("��������:" + i);
			// submit�Ǹ�block����
			publiser.submit(i);
		}

		// 5. ������ �رշ�����
		// ��ʽ���� Ӧ�÷� finally ����ʹ�� try-resouce ȷ���ر�
		publiser.close();

		// ���߳��ӳ�ֹͣ, ��������û�����Ѿ��˳�
		Thread.currentThread().join(1000);
		// debug��ʱ��, ����������Ҫ�жϵ�
		// �������߳̽����޷�debug
		System.out.println();
	}

}
