import java.util.ArrayList;
import java.util.List;

public class WildcardType {
    private static class Animal {
        void breathe() {
            System.out.println("スーハー");
        }
    }

    private static class Cat extends Animal {
        void meow() {
            System.out.println("ニャー");
        }
    }

    private static class Dog extends Animal {
        void bark() {
            System.out.println("ワンワン");
        }
    }

    private static class ShibaInu extends Dog {
        void shiba() {
            System.out.println("シバシバ");
        }
    }

    private static class Pug extends Dog {
        void pug() {
            System.out.println("パグパグ");
        }
    }
    /*

     Object
       |
     Animal
     /    \
  Cat      Dog
     \     /  \
      \  Pug  ShibaInu
       \  |  /
         null

     */

    // Dog の部分型の List を全て許容
    private static void readingContext(List<? extends Dog> dogs) {
        for (Dog dog : dogs) {
            dog.bark();
        }
    }

    // Dog の上位型の List を全て許容
    private static void addingContext(List<? super Dog> dogs) {
        dogs.add(new Dog());
        dogs.add(new ShibaInu());
        dogs.add(new Pug());
    }

    public static void main(String[] args) {
        System.out.println("##########################################");
        System.out.println("## 値の取得元としてのみ使う場合の共変性 ##");
        System.out.println("##########################################");

        List<Dog> dogs1 = List.of(new Dog(), new ShibaInu(), new Pug());
        readingContext(dogs1); // OK

        List<ShibaInu> shibainus1 = List.of(new ShibaInu(), new ShibaInu());
        readingContext(shibainus1); // OK。ワイルドカードなしでは拒否されていたが安全。

        List<Animal> animals1 = List.of(new Animal(), new Cat(), new Dog(), new ShibaInu(), new Pug());
        // readingContext(animals1); // コンパイルエラー。これは理論的に型安全でないので妥当。

        System.out.println("##########################################");
        System.out.println("## 値の渡し先としてのみ使う場合の反変性 ##");
        System.out.println("##########################################");

		List<Dog> dogs2 = new ArrayList<>();
        addingContext(dogs2); // OK
        dogs2.forEach(Dog::bark);

        List<Animal> animals2 = new ArrayList<>();
        addingContext(animals2); // OK。ワイルドカードなしでは拒否されていたが安全。
        animals2.forEach(Animal::breathe);

        List<ShibaInu> shibainus2 = new ArrayList<>();
        // addingContext(shibainus2); // コンパイルエラー。これは理論的に型安全でないので妥当。
        shibainus2.forEach(ShibaInu::shiba); // shibainus2 内には、shiba メソッドを持たない者は存在してはならない。
    }
}
