Hra dáma v Javě

Projekt do předmětu IJA 2013

Tým "Roman č#!?@"
Členové
    Hála Pavel
    Lukš Roman

 Pravidla hry

    hraje se na šachovnici 8x8 se střídající se barvou polí (černá a bílá) a to pouze na černých polích; sloupce jsou označeny písmeny (a-h), řádky jsou označeny čísly (1-8); každé pole je identifikováno dvojicí písmeno číslo (např. a1); na pozici a1 je černá barva
    před zahájením partie je dvanáct černých kamenů umístěno na polích a7, b6, b8, c7, d6, d8, e7, f6, f8, g7, h6, h8 a dvanáct bílých kamenů na polích a1, a3, b2, c1, c3, d2, e1, e3, f2, g1, g3, h2; pole na 4. a 5. řadě desky jsou volná
    každý tah je tvořen krokem bílého a krokem černého; bílý vždy začíná
    kámen se pohybuje vždy jen dopředu po diagonále z jednoho pole na druhé volné pole další řady (pozn.: dopředu z pohledu hráče, tj. bílý se pohybuje od 1. řady k 8. řadě a černý od 8. řady k 1. řadě)
    pokud kámen dosáhne základní řady na soupeřově straně (tj. bílý 8. řady a černý 1. řady), stává se z něj dáma; dáma se přesunuje dozadu a dopředu na libovolná volná pole diagonály, na které se nachází
    braní figur
        braní soupeřovy figury je povinné, kamenem se provádí jen dopředu, dámou lze brát i dozadu; braní nebo přeskakování vlastních figur je zakázané.
        braní kamenem: jestliže se kámen nachází na diagonále v sousedství soupeřovy figury, za kterou je volné pole, je povinen ji přeskočit, obsadit toto volné pole a odstranit přeskočenou soupeřovu figuru z desky
        braní dámou: jestliže se dáma nachází na stejné diagonále se soupeřovou figurou, za kterou je jedno nebo více volných polí, je povinna tuto figuru přeskočit, zaujmout libovolné volné pole na diagonále za ní a přeskočenou figuru odstranit z desky.
        podrobná pravidla viz např. http://dama.blgz.cz/ (není třeba implementovat pravidla přesně, postačuje výše uvedené)
    základní notace pro zápis partie
        pořadové číslo tahu, tah bílého, tah černého
        tah bílého nebo černého se zapisuje ve formátu odkud-kam (např. c3-b4)
        pokud došlo při přemístění figury k braní, zapíše se místo - znak x (např. e5xg3)
        příklad zápisu celého tahu: 1. c3-b4 b6-c5