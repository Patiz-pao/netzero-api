package com.netzero.version.demo.domain.Enums;

import static com.netzero.version.demo.Util.Constants.*;

public enum RiceActivityType {
    STORE_ELECTRICITY("กักเก็บไฟฟ้า","", 0, 7),
    PLOWING_FIRST("ไถดะ (รอบที่ 1)","ใช้รถไถนาไฟฟ้าไถดินลึก 15 - 20 ซม. เพื่อพลิกดินและทำลายวัชพืช หลังไถ ทิ้งดินไว้ให้วัชพืชย่อยสลาย", USE_TRACTOR, 1),
    REST_SOIL("พักดิน 5 วัน","พักดินทิ้งไว้ 5 วัน เพื่อให้ดินปรับตัวและวัชพืชที่ตายย่อยสลาย", 0, 5),
    PLOWING_SECOND("ไถแปร (รอบที่ 2)","ใช้รถไถนาไฟฟ้าไถอีกครั้งเพื่อย่อยดินและกำจัดวัชพืช ปรับระดับพื้นที่ให้เรียบ", USE_TRACTOR, 1),
    REST_SOIL_SECOND("พักดิน 1 วัน","พักดินทิ้งไว้ 1 วันเพื่อเตรียมการสูบน้ำ", 0, 1),
    HARROW_FIRST_AND_PUMPING_WATER_FIRST("คราด (ครั้งที่ 1), สูบน้ำ (ครั้งที่ 1)","ใช้คราดดินปรับหน้าดินให้เรียบเสมอ ทำทันทีหลังการไถแปร (1-2 วันก่อนหว่านเมล็ด) และ ใช้เครื่องสูบน้ำไฟฟ้า สูบน้ำเข้านาให้ดินชุ่มชื้น ระดับน้ำ 3-5 ซม.", USE_TRACTOR + USE_WATER_PUMP, 1),
    DRONE_FIRST("ใช้โดรนหว่านเมล็ดพันธุ์ (ครั้งที่1)","ช่วงการหว่านเมล็ดพันธุ์ หว่านเมล็ดพันธุ์ กข47 ใช้เมล็ดพันธุ์ที่แช่น้ำและพักจนรากเริ่มงอก หว่านด้วยโดรนหว่านเมล็ดพันธุ์", USE_DRONE, 1),
    STORE_ELECTRICITY_SECOND("กักเก็บไฟฟ้า","", 0, 6),
    PUMPING_WATER_SECOND("สูบน้ำ (ครั้งที่ 2)","ช่วงการดูแลต้นข้าว รักษาระดับน้ำในนา 3 - 5 ซม. ตลอดระยะการเจริญเติบโต สูบน้ำเข้านา ( ครั้งที่ 2 ) หลังหว่าน 7 วัน เพื่อกระตุ้นการงอก", USE_WATER_PUMP, 1),
    STORE_ELECTRICITY_THREE("กักเก็บไฟฟ้า","", 0, 7),
    DRONE_SECOND("ใช้โดรนพ่นปุ๋ย (ครั้งที่1)","พ่นปุ๋ย ( ครั้งที่ 1 ) อายุข้าว 15 วัน ( เร่งการเจริญเติบโตของต้นอ่อน )", USE_DRONE, 1),
    STORE_ELECTRICITY_FOUR("กักเก็บไฟฟ้า","", 0, 1),
    PUMPING_WATER_THREE("สูบน้ำ (ครั้งที่ 3)","ใช้เครื่องสูบน้ำไฟฟ้า สูบน้ำเข้านาให้ดินชุ่มชื้น ระดับน้ำ 3-5 ซม.", USE_WATER_PUMP, 1),
    STORE_ELECTRICITY_FIVE("กักเก็บไฟฟ้า","", 0, 9),
    PUMPING_WATER_FOUR("สูบน้ำ (ครั้งที่ 4)","ใช้เครื่องสูบน้ำไฟฟ้า สูบน้ำเข้านาให้ดินชุ่มชื้น ระดับน้ำ 3-5 ซม.", USE_WATER_PUMP, 1),
    STORE_ELECTRICITY_SIX("กักเก็บไฟฟ้า","", 0, 9),
    PUMPING_WATER_FIVE("สูบน้ำ (ครั้งที่ 5)","ใช้เครื่องสูบน้ำไฟฟ้า สูบน้ำเข้านาให้ดินชุ่มชื้น ระดับน้ำ 3-5 ซม.", USE_WATER_PUMP, 1),
    STORE_ELECTRICITY_SEVEN("กักเก็บไฟฟ้า","", 0, 8),
    DRONE_THREE("การพ่นปุ๋ยด้วยโดรน (ครั้งที่ 2)","พ่นปุ๋ย ( ครั้งที่ 2 ) อายุข้าว 45 วัน ( บำรุงต้นและใบ )", USE_DRONE, 1),
    PUMPING_WATER_SIX("สูบน้ำ (ครั้งที่ 6)","", USE_WATER_PUMP, 1),
    STORE_ELECTRICITY_EIGHT("กักเก็บไฟฟ้า","", 0, 9),
    PUMPING_WATER_SEVEN("สูบน้ำ (ครั้งที่ 7)","ใช้เครื่องสูบน้ำไฟฟ้า สูบน้ำเข้านาให้ดินชุ่มชื้น ระดับน้ำ 3-5 ซม.", USE_WATER_PUMP, 1),
    STORE_ELECTRICITY_NINE("กักเก็บไฟฟ้า","", 0, 9),
    PUMPING_WATER_EIGHT("สูบน้ำ (ครั้งที่ 8)","ใช้เครื่องสูบน้ำไฟฟ้า สูบน้ำเข้านาให้ดินชุ่มชื้น ระดับน้ำ 3-5 ซม.", USE_WATER_PUMP, 1),
    STORE_ELECTRICITY_TEN("กักเก็บไฟฟ้า","", 0, 2),
    DRONE_FOUR("การพ่นปุ๋ยด้วยโดรน ( ครั้งที่ 3 )","พ่นปุ๋ย ( ครั้งที่ 3 ) อายุข้าว 70 วัน ( ช่วยเร่งการสร้างเมล็ด )", USE_DRONE, 1),
    STORE_ELECTRICITY_ELEVEN("กักเก็บไฟฟ้า","", 0, 6),
    PUMPING_WATER_NINE("สูบน้ำ (ครั้งที่ 9)","ใช้เครื่องสูบน้ำไฟฟ้า สูบน้ำเข้านาให้ดินชุ่มชื้น ระดับน้ำ 3-5 ซม.", USE_WATER_PUMP, 1),
    STORE_ELECTRICITY_TWELVE("กักเก็บไฟฟ้า","", 0, 9),
    PUMPING_WATER_TEN("สูบน้ำ (ครั้งที่ 10)","ใช้เครื่องสูบน้ำไฟฟ้า สูบน้ำเข้านาให้ดินชุ่มชื้น ระดับน้ำ 3-5 ซม.", USE_WATER_PUMP, 1),
    STORE_ELECTRICITY_THIRTEEN("รอระบายน้ำออกจากนา","ช่วงรอระบายน้ำออกจากนาประมาณ 16 วัน", 0, 16),
    WAITING_TO_HARVEST("ระบายน้ำออกจากนา","ระบายน้ำออกจากนาประมาณ 6 วัน", 0, 6),
    HARVEST("เก็บเกี่ยวข้าว","เก็บเกี่ยวผลผลิต", 0, 4);



    private final String name;
    private final String description;
    private final double electricityRequired;
    private final int duration;

    RiceActivityType(String name, String description, double electricityRequired, int duration) {
        this.name = name;
        this.description = description;
        this.electricityRequired = electricityRequired;
        this.duration = duration;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getElectricityRequired() { return electricityRequired; }
    public int getDuration() { return duration; }
}
