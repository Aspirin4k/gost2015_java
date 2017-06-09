package gost_kuznechik;

public class Gost_kuznechik {

    // Мастер ключ
    private final static byte[] MASTER_KEY = {
        (byte)0x88, (byte)0x99, (byte)0xaa, (byte)0xbb, (byte)0xcc, 
        (byte)0xdd, (byte)0xee, (byte)0xff, (byte)0x00, (byte)0x11,
        (byte)0x22, (byte)0x33, (byte)0x44, (byte)0x55, (byte)0x66, 
        (byte)0x77, (byte)0xfe, (byte)0xdc, (byte)0xba, (byte)0x98, 
        (byte)0x76, (byte)0x54, (byte)0x32, (byte)0x10, (byte)0x01, 
        (byte)0x23, (byte)0x45, (byte)0x67, (byte)0x89, (byte)0xab, 
        (byte)0xcd, (byte)0xef
    };
    
    // Размер блока
    private final static int BLOCK_SIZE=16;
    
    // Сессионные ключи
    private final static byte[][] SESSION_KEYS = new byte[10][];
    
    // Модуль, по которому производятся операции в поле Галуа
    private final static short GALUA_MODULO = 451;
    
    // S-блок
    private final static byte[] S = {
        (byte)252, (byte)238, (byte)221, (byte) 17, (byte)207, (byte)110, (byte) 49, (byte) 22, (byte)251,
        (byte)196, (byte)250, (byte)218, (byte) 35, (byte)197, (byte)  4, (byte) 77, (byte)233, (byte)119, 
        (byte)240, (byte)219, (byte)147, (byte) 46, (byte)153, (byte)186, (byte) 23, (byte) 54, (byte)241, 
        (byte)187, (byte) 20, (byte)205, (byte) 95, (byte)193, (byte)249, (byte) 24, (byte)101, (byte) 90,
        (byte)226, (byte) 92, (byte)239, (byte) 33, (byte)129, (byte) 28, (byte) 60, (byte) 66, (byte)139,
        (byte)  1, (byte)142, (byte) 79, (byte)  5, (byte)132, (byte)  2, (byte)174, (byte)227, (byte)106,
        (byte)143, (byte)160, (byte)  6, (byte) 11, (byte)237, (byte)152, (byte)127, (byte)212, (byte)211,
        (byte) 31, (byte)235, (byte) 52, (byte) 44, (byte) 81, (byte)234, (byte)200, (byte) 72, (byte)171, 
        (byte)242, (byte) 42, (byte)104, (byte)162, (byte)253, (byte) 58, (byte)206, (byte)204, (byte)181,
        (byte)112, (byte) 14, (byte) 86, (byte)  8, (byte) 12, (byte)118, (byte) 18, (byte)191, (byte)114,
        (byte) 19, (byte) 71, (byte)156, (byte)183, (byte) 93, (byte)135, (byte) 21, (byte)161, (byte)150, 
        (byte) 41, (byte) 16, (byte)123, (byte)154, (byte)199, (byte)243, (byte)145, (byte)120, (byte)111,
        (byte)157, (byte)158, (byte)178, (byte)177, (byte) 50, (byte)117, (byte) 25, (byte) 61, (byte)255,
        (byte) 53, (byte)138, (byte)126, (byte)109, (byte) 84, (byte)198, (byte)128, (byte)195, (byte)189,
        (byte) 13, (byte) 87, (byte)223, (byte)245, (byte) 36, (byte)169, (byte) 62, (byte)168, (byte) 67, 
        (byte)201, (byte)215, (byte)121, (byte)214, (byte)246, (byte)124, (byte) 34, (byte)185, (byte)  3,
        (byte)224, (byte) 15, (byte)236, (byte)222, (byte)122, (byte)148, (byte)176, (byte)188, (byte)220,
        (byte)232, (byte) 40, (byte) 80, (byte) 78, (byte) 51, (byte) 10, (byte) 74, (byte)167, (byte)151,
        (byte) 96, (byte)115, (byte) 30, (byte)  0, (byte) 98, (byte) 68, (byte) 26, (byte)184, (byte) 56,
        (byte)130, (byte)100, (byte)159, (byte) 38, (byte) 65, (byte)173, (byte) 69, (byte) 70, (byte)146,
        (byte) 39, (byte) 94, (byte) 85, (byte) 47, (byte)140, (byte)163, (byte)165, (byte)125, (byte)105,
        (byte)213, (byte)149, (byte) 59, (byte)  7, (byte) 88, (byte)179, (byte) 64, (byte)134, (byte)172,
        (byte) 29, (byte)247, (byte) 48, (byte) 55, (byte)107, (byte)228, (byte)136, (byte)217, (byte)231,
        (byte)137, (byte)225, (byte) 27, (byte)131, (byte) 73, (byte) 76, (byte) 63, (byte)248, (byte)254,
        (byte)141, (byte) 83, (byte)170, (byte)144, (byte)202, (byte)216, (byte)133, (byte) 97, (byte) 32,
        (byte)113, (byte)103, (byte)164, (byte) 45, (byte) 43, (byte)  9, (byte) 91, (byte)203, (byte)155,
        (byte) 37, (byte)208, (byte)190, (byte)229, (byte)108, (byte) 82, (byte) 89, (byte)166, (byte)116,
        (byte)210, (byte)230, (byte)244, (byte)180, (byte)192, (byte)209, (byte)102, (byte)175, (byte)194,
        (byte) 57, (byte) 75, (byte) 99, (byte)182
    };
            
    /**
     * Нелинейная подстановка
     * @param blocks блоки
     */
    private static void nonLinear(byte[] blocks)
    {
        for (int i=0; i<BLOCK_SIZE; i++)
        {
            blocks[i] = S[blocks[i] < 0 ? 256+blocks[i] : blocks[i]];
        }
    }
    
    /**
     * Обратное нелинейное преобразование
     * @param blocks блоки
     */
    private static void nonLinear_reverse(byte[] blocks)
    {
        for (int i=0; i<BLOCK_SIZE; i++)
        {
            int j=0;
            while (S[j] != blocks[i])
                j++;
            blocks[i]=(byte)j;
        }
    }
    
    /**
     * Скомпроментированное нелинейное преобразование
     * @param blocks блоки
     * @param fake_pos номер зануленного байта
     */
    private static void nonLinear_incorrect(byte[] blocks, int fake_pos)
    {
        for (int i=0; i<BLOCK_SIZE; i++)
        {
            if (i != fake_pos)
                blocks[i] = S[blocks[i] < 0 ? 256+blocks[i] : blocks[i]];
            else
                blocks[i] = 0;
        }
    }
    
    /**
     * Линейное преобразование
     * @param blocks блоки
     * @return преобразование
     */
    private static byte[] Linear_R(byte[] blocks)
    {
        byte[] result = new byte[BLOCK_SIZE];
        result[0] = phi(blocks);
        
        for (int i=1; i<BLOCK_SIZE; i++)
        {
            result[i]=blocks[i-1];
        }
        
        return result;
    }
    
    /**
     * Обратное линейное преобразование
     * @param blocks блок, который преобразуют
     * @return преобразованный блок
     */
    private static byte[] Linear_R_reverse(byte[] blocks)
    {
        byte[] result = new byte[BLOCK_SIZE];
        byte[] buffer = new byte[BLOCK_SIZE];
        for (int i=1; i<BLOCK_SIZE; i++)
            buffer[i-1] = blocks[i];
        buffer[15] = blocks[0];
        
        result[15]=phi(buffer);
        for (int i=0; i<BLOCK_SIZE-1; i++)
            result[i] = blocks[i+1];
        
        return result;
    }
    
    /**
     * R преобразование 16 раз
     * @param blocks блок
     */
    private static void Linear_L(byte[] blocks)
    {
        byte[] buffer = blocks;
        for (int i=0; i<16; i++)
        {
            buffer = Linear_R(buffer);
        }
        
        System.arraycopy(buffer, 0, blocks, 0, BLOCK_SIZE);
    }
    
    /**
     * Обратное линейное преобразование
     * @param blocks 
     */
    private static void Linear_L_reverse(byte[] blocks)
    {
        byte[] buffer = blocks;
        for (int i=0; i<16; i++)
        {
            buffer = Linear_R_reverse(buffer);
        }
        
        System.arraycopy(buffer, 0, blocks, 0, BLOCK_SIZE);
    }
    
    /**
     * Некоторые преобразования над полем Галуа
     * @param blocks блоки
     * @return байтовое отображение
     */
    private static byte phi(byte[] blocks)
    {
        return sumOverGalua(
                sumOverGalua(
                    sumOverGalua(
                        sumOverGalua(
                                multOverGalua((byte)148, blocks[0]),
                                multOverGalua((byte) 32, blocks[1])
                        ),
                        sumOverGalua(
                                multOverGalua((byte)133, blocks[2]),
                                multOverGalua((byte) 16, blocks[3])
                        )
                    ),
                    sumOverGalua(
                        sumOverGalua(
                                multOverGalua((byte)194, blocks[4]),
                                multOverGalua((byte)192, blocks[5])
                        ),
                        sumOverGalua(
                                multOverGalua((byte)  1, blocks[6]),
                                multOverGalua((byte)251, blocks[7])
                        )
                    )
                ),
                sumOverGalua(
                    sumOverGalua(
                        sumOverGalua(
                                multOverGalua((byte)  1, blocks[8]),
                                multOverGalua((byte)192, blocks[9])
                        ),
                        sumOverGalua(
                                multOverGalua((byte)194, blocks[10]),
                                multOverGalua((byte) 16, blocks[11])
                        )
                    ),
                    sumOverGalua(
                        sumOverGalua(
                                multOverGalua((byte)133, blocks[12]),
                                multOverGalua((byte) 32, blocks[13])
                        ),
                        sumOverGalua(
                                multOverGalua((byte)148, blocks[14]),
                                multOverGalua((byte)  1, blocks[15])
                        )
                    )
                )
                );
    }
    
    /**
     * Сумма над полем Галуа
     * @param a первое число
     * @param b второе число
     * @return сумма по модулю
     */
    private static byte sumOverGalua(byte a, byte b)
    {
        return (byte)(a ^ b);
    }
    
    /**
     * Произведение над полем Галуа
     * @param a первое число
     * @param b второе число
     * @return произведение по модулю
     */
    private static byte multOverGalua(byte a, byte b)
    {
        short result=0;
        for (int i=0; i<8; i++)
        {
            for (int j=0; j<8; j++)
            {
                // Если разряды i и j равны 1
                if (((a>>i) % 2 != 0) && ((b>>j) % 2 != 0))
                    result = (short)(result ^ (1 << (i+j)));
            }
        }
        
        return moduloOverGalua(result);
    }
    
    /**
     * Расчет модуля в поле галуа
     * @param a число
     * @return модуль от числа
     */
    private static byte moduloOverGalua(short a)
    {
        byte mostSignificantDigitModulo=0;
        byte mostSignificantDigitA=0;
        for (byte i=15; (i>=0) && (mostSignificantDigitModulo == 0); i--)
            if ((GALUA_MODULO >> i) % 2 != 0) mostSignificantDigitModulo=i;
        
        while ((a >> mostSignificantDigitModulo) != 0)
        {
            // Находим самый левый разряд текущего числа
            for (byte i=15; (i>=0) && (mostSignificantDigitA == 0); i--)
                if ((a>>i) % 2 != 0) mostSignificantDigitA=i;
            
            // Смещаем модуль влево на разницу в самых левых разрядах и ксорим
            a = (short)((GALUA_MODULO << (mostSignificantDigitA-mostSignificantDigitModulo)) ^ a);
            mostSignificantDigitA = 0;
        }
        
        return (byte)a;
    }
    
    /**
     * Исключающее или с сессионным ключом
     * @param blocks блок
     * @param key сессионный ключ
     */
    private static void xor(byte[] blocks, byte[] key)
    {
        for (int i=0; i<BLOCK_SIZE; i++)
            blocks[i] = (byte)(blocks[i] ^ key[i]);
    }
    
    /**
     * Генерация сессионных ключей
     */
    private static void keyDeploy()
    {
        SESSION_KEYS[0] = new byte[BLOCK_SIZE];
        System.arraycopy(MASTER_KEY, 0, SESSION_KEYS[0], 0, BLOCK_SIZE);
        
        SESSION_KEYS[1] = new byte[BLOCK_SIZE];
        for (int i=0; i<BLOCK_SIZE; i++)
            SESSION_KEYS[1][i] = MASTER_KEY[i+BLOCK_SIZE];
        
        // Итерационные константы
        byte[] iteratorConst = new byte[BLOCK_SIZE];
        
        // Генерация остальных ключей
        for (int i=2; i<10; i+=2)
        {
            // При генерации новых ключей используются предыдущие
            SESSION_KEYS[i] = new byte[BLOCK_SIZE];
            System.arraycopy(SESSION_KEYS[i-2], 0, SESSION_KEYS[i], 0, BLOCK_SIZE);
            
            SESSION_KEYS[i+1] = new byte[BLOCK_SIZE];
            System.arraycopy(SESSION_KEYS[i-1], 0, SESSION_KEYS[i+1], 0, BLOCK_SIZE);
            
            // 8 раундов с итерационным ключами
            for (int j=0; j<8; j++)
            {
                for (int z=0; z<BLOCK_SIZE-1; z++)
                    iteratorConst[z] = 0;
                iteratorConst[15] = (byte)(8*(i/2-1) + j+1);
                Linear_L(iteratorConst);
                F(iteratorConst,SESSION_KEYS[i],SESSION_KEYS[i+1]);
            }
        }
    }
    
    private static void F(byte[] key, byte[] a, byte[] b)
    {
        byte[] buffer = new byte[BLOCK_SIZE];
        System.arraycopy(a, 0, buffer, 0, BLOCK_SIZE);
        
        // Сложение по модулю с сессионным ключом
        xor(a, key);
        // Подстановка
        nonLinear(a);
        // Линейное преобразование
        Linear_L(a);
        // Сложение по модулю с парой
        xor(a, b);
        // a выходит через b без изменений
        System.arraycopy(buffer, 0, b, 0, BLOCK_SIZE);
    }
    
    /**
     * Шифрование блока текста
     * @param a 
     */
    public static void Encode(byte[] a)
    {
        for (int i=0; i<9; i++)
        {
            xor(a, SESSION_KEYS[i]);
            nonLinear(a);
            Linear_L(a);
        }
        xor(a, SESSION_KEYS[9]);
    }
    
    /**
     * Ложное шифрование
     * @param a 
     * @param pos позиция, для искажения s блока
     */
    public static void fakeEncode(byte[] a, int pos)
    {
        // 1-й раунд. Если pos<16, то обнуляем соответствующий байт
        xor(a, SESSION_KEYS[0]);
        if (pos < 16)
            nonLinear_incorrect(a,pos);
        else
            nonLinear(a);
        Linear_L(a);
        
        // 2-й раунд. Если pos>=16, то обнуляем байт pos-16
        xor(a, SESSION_KEYS[1]);
        if (pos >= 16)
            nonLinear_incorrect(a, pos - 16);
        else
            nonLinear(a);
        Linear_L(a);
        
        // Остальное шифрование незатронуто
        for (int i=2; i<9; i++)
        {
            xor(a, SESSION_KEYS[i]);
            nonLinear(a);
            Linear_L(a);
        }
        xor(a, SESSION_KEYS[9]);
    }
    
    /**
     * Расшифрование текста
     * @param a блок
     */
    public static void Decode(byte[] a)
    {
        // 9 раундов
        for (int i=0; i<9; i++)
        {
            xor(a, SESSION_KEYS[9-i]);
            Linear_L_reverse(a);
            nonLinear_reverse(a);
        }
        // Последний раунд только xor
        xor(a, SESSION_KEYS[0]);
    }
    
    /**
     * Сравнение двух массивов
     * @param a первый массив
     * @param b второй массив
     * @return 
     */
    public static boolean equals(byte[] a, byte[] b)
    {
        boolean result=true;
        
        for (int i=0; (i<BLOCK_SIZE) && result; i++)
            result = a[i] == b[i];
        
        return result;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        // Разворачиваем ключи
        keyDeploy();
        // Входной блок
//        byte[] a = {
//            (byte)0x11, (byte)0x22, (byte)0x33, (byte)0x44, (byte)0x55, (byte)0x66,
//            (byte)0x77, (byte)0x00, (byte)0xff, (byte)0xee, (byte)0xdd, (byte)0xcc,
//            (byte)0xbb, (byte)0xaa, (byte)0x99, (byte)0x88
//        };
        
        byte[] encoded = new byte[BLOCK_SIZE];
        byte[] fake_encoded = new byte[BLOCK_SIZE];  

        byte S_reverse = 0;
        while (S[S_reverse>=0 ? S_reverse : 256+S_reverse]!=0) S_reverse++;
        
        byte[] reconstructedKey = new byte[BLOCK_SIZE];
        
        int output=0;
        // Для каждого байта
        for (int z=0; z< 32; z++)
        {
            byte j=0;
            do
            {
                for (int i=0; i<BLOCK_SIZE; i++)
                {
                    encoded[i] = 0;
                    fake_encoded[i] = 0;
                }
                encoded[z%16] = j;
                fake_encoded[z%16] = j;

//                for (int i=0; i<BLOCK_SIZE; i++)
//                    System.out.print(encoded[i] + " ");
//                System.out.println();
                
                Encode(encoded);
                fakeEncode(fake_encoded,z);

                j++;  
            } while (!equals(encoded, fake_encoded));
            
            // Восстанавливаем второй сессионый ключ или первый
            if (z>=16)
            {
                for (int i=0; i<BLOCK_SIZE; i++)
                {
                    encoded[i] = 0;
                }
                encoded[z-16] = (byte)(j-1);

                xor(encoded,reconstructedKey);
                nonLinear(encoded);
                Linear_L(encoded);
                j=encoded[z-16];
            }
            else
            {
                j--;
                reconstructedKey[z] = (byte)(j ^ S_reverse);
            }
            
            if (z % 4 == 3)
            {
                output = (output << 8) ^ ((j ^ S_reverse) & 255);
                System.out.print(Integer.toHexString(output));
                output=0;
            }
            else
            {
                output = (output << 8) ^ ((j ^ S_reverse) & 255);
            }
        }
        
        System.out.println();
        
    }
    
}
