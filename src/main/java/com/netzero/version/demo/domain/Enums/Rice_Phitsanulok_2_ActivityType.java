package com.netzero.version.demo.domain.Enums;

import com.netzero.version.demo.domain.Enums.interfaceClass.RiceActivityType;
import lombok.Getter;

import static com.netzero.version.demo.Util.Constants.*;

@Getter
public enum Rice_Phitsanulok_2_ActivityType implements RiceActivityType {
    PLOWING_FIRST("ไถดะ","ใช้รถไถนาไฟฟ้าไถดินลึก 15 - 20 ซม. เพื่อพลิกดินและทำลายวัชพืช หลังไถ ทิ้งดินไว้ให้วัชพืชย่อยสลาย", USE_TRACTOR, 1),
    REST_SOIL("ช่วงพักดิน","พักดินทิ้งไว้ 5 วัน เพื่อให้ดินปรับตัวและวัชพืชที่ตายย่อยสลาย", 0, 5),
    PLOWING_SECOND("ไถแปร","ใช้รถไถนาไฟฟ้าไถอีกครั้งเพื่อย่อยดินและกำจัดวัชพืช ปรับระดับพื้นที่ให้เรียบ", USE_TRACTOR, 1),
    REST_SOIL_SECOND("พักดิน","ช่วงพักดินเพื่อเตรียมกำจัดวัชพืชอีกครั้ง และเตรียมการสูบน้ำ", 0, 1),
    HARROW_FIRST("คราด(ครั้งที่ 1)","ใช้คราดดินปรับหน้าดินให้เรียบเสมอ ทำทันทีหลังการไถแปร (1-2 วันก่อนหว่านเมล็ด) ", USE_TRACTOR , 1),
    PUMPING_WATER_FIRST("สูบน้ำ(ครั้งที่ 1)","ใช้เครื่องสูบน้ำไฟฟ้า สูบน้ำเข้านาให้ดินชุ่มชื้น ระดับน้ำ 3-5 ซม.",USE_WATER_PUMP,0),
    DRONE_FIRST("ช่วงการหว่านเมล็ดพันธุ์","ช่วงการหว่านเมล็ดพันธุ์ ใช้โดรนหว่านเมล็ดพันธุ์ หว่านเมล็ดพันธุ์ กข47 ใช้เมล็ดพันธุ์ที่แช่น้ำและพักจนรากเริ่มงอก หว่านด้วยโดรนหว่านเมล็ดพันธุ์", USE_DRONE, 1),
    STORE_ELECTRICITY_SECOND("ช่วงรอข้าวแตกกอ","หากน้ำแห้ง ควรสูบน้ำเติมเพื่อป้องกันไม่ให้ข้าวขาดน้ำ แต่ไม่ควรปล่อยให้น้ำขังลึกเกินไป", 0, 6),
    PUMPING_WATER_SECOND("สูบน้ำเข้านา ( ครั้งที่ 2 ) ช่วงการดูแลต้นข้าว","หลังหว่าน 7 วัน เพื่อกระตุ้นการงอก รักษาระดับน้ำในนา 3 - 5 ซม. ตลอดระยะการเจริญเติบโต", USE_WATER_PUMP, 1),
    STORE_ELECTRICITY_THREE("ช่วงระยะข้าวแตกกอ","เป็นช่วงสำคัญที่ต้องใส่ใจในการบำรุง ถ้าต้นข้าวแตกกอได้ดี ได้กอเยอะ ปริมาณข้าวก็จะได้เยอะตาม", 0, 7),
    DRONE_SECOND("การพ่นปุ๋ยด้วยโดรน","พ่นปุ๋ย ( ครั้งที่ 1 ) ใช้ปุ๋ยสูตร 16-20-0 หรือ 46-0-0 เพื่อกระตุ้นการเจริญเติบโตและการแตกกอ", USE_DRONE, 1),
    STORE_ELECTRICITY_FOUR("ช่วงการดูแลข้าวระยะแตกกอ","รักษาระดับน้ำในนาให้สูงประมาณ 3-5 ซม เพื่อไม่ให้ข้าวขาดน้ำ", 0, 1),
    PUMPING_WATER_THREE("สูบน้ำ (ครั้งที่ 3)","ใช้เครื่องสูบน้ำไฟฟ้า สูบน้ำเข้านาให้ดินชุ่มชื้น ระดับน้ำ 3-5 ซม.", USE_WATER_PUMP, 1),
    STORE_ELECTRICITY_FIVE("ช่วงการดูแลข้าวระยะแตกกอ","กำจัดวัชพืชที่งอกขึ้นในแปลงนา เพราะวัชพืชจะแย่งธาตุอาหาร น้ำ และแสงแดดจากต้นข้าว", 0, 9),
    PUMPING_WATER_FOUR("สูบน้ำ (ครั้งที่ 4)","ใช้เครื่องสูบน้ำไฟฟ้า สูบน้ำเข้านาให้ดินชุ่มชื้น ระดับน้ำ 3-5 ซม.", USE_WATER_PUMP, 1),
    STORE_ELECTRICITY_SIX("ช่วงการดูแลข้าวระยะแตกกอ","สังเกตต้นข้าวว่ามีแมลงศัตรูพืช เช่น เพลี้ยไฟ หรือโรคใบไหม้หรือไม่ หากพบปัญหา ให้ใช้สารชีวภัณฑ์หรือสารป้องกันโรคที่เหมาะสม", 0, 9),
    PUMPING_WATER_FIVE("สูบน้ำ (ครั้งที่ 5)","ใช้เครื่องสูบน้ำไฟฟ้า สูบน้ำเข้านาให้ดินชุ่มชื้น ระดับน้ำ 3-5 ซม.", USE_WATER_PUMP, 1),
    STORE_ELECTRICITY_SEVEN("เตรียมเข้าสู่ช่วงข้าวตั้งท้อง","เป็นช่วงที่ต้นข้าวเริ่มสร้างรวงข้าวภายในลำต้น ก่อนที่จะออกรวงให้เห็น", 0, 8),
    DRONE_THREE("การพ่นปุ๋ยด้วยโดรน (ครั้งที่ 2)","พ่นปุ๋ย ( ครั้งที่ 2 ) อายุข้าว 45 วัน ใช้ปุ๋ยสูตร 16-16-8 หรือ 15-15-15 เพื่อช่วยกระตุ้นการพัฒนารวง เสริมความแข็งแรงของต้นข้าว", USE_DRONE, 1),
    PUMPING_WATER_SIX("สูบน้ำ (ครั้งที่ 6)","", USE_WATER_PUMP, 1),
    STORE_ELECTRICITY_EIGHT("การดูแลข้าวในระยะตั้งท้อง","ห้ามขาดน้ำ เพราะจะทำให้รวงไม่สมบูรณ์", 0, 9),
    PUMPING_WATER_SEVEN("สูบน้ำ (ครั้งที่ 7)","ใช้เครื่องสูบน้ำไฟฟ้า สูบน้ำเข้านาให้ดินชุ่มชื้น ระดับน้ำ 3-5 ซม.", USE_WATER_PUMP, 1),
    STORE_ELECTRICITY_NINE("การดูแลข้าวในระยะตั้งท้อง","ระวังโรคในระยะตั้งท้อง เช่น โรคใบไหม้ โรคเมล็ดด่าง", 0, 9),
    PUMPING_WATER_EIGHT("สูบน้ำ (ครั้งที่ 8)","ใช้เครื่องสูบน้ำไฟฟ้า สูบน้ำเข้านาให้ดินชุ่มชื้น ระดับน้ำ 3-5 ซม.", USE_WATER_PUMP, 1),
    STORE_ELECTRICITY_TEN("การดูแลข้าวในระยะตั้งท้อง","ตรวจสอบแมลงศัตรู เช่น เพลี้ยกระโดดหรือหนอนกอ", 0, 2),
    DRONE_FOUR("การพ่นปุ๋ยด้วยโดรน","พ่นปุ๋ย ( ครั้งที่ 3 ) อายุข้าว 70 วัน ใส่ปุ๋ยยูเรีย 46-0-0 หรือ 13-13-2 เพื่อเร่งการสะสมอาหารในเมล็ด", USE_DRONE, 1),
    STORE_ELECTRICITY_ELEVEN("ช่วงระยะข้าวออกรวง","รวงข้าวเริ่มแทงออกมาจากปล้องสุดท้าย (ปล้องคอรวง) เมล็ดในรวงเริ่มพัฒนา โดยจะเป็นระยะที่ต้นข้าวใช้พลังงานและสารอาหารสูง", 0, 6),
    PUMPING_WATER_NINE("สูบน้ำ (ครั้งที่ 9)","ใช้เครื่องสูบน้ำไฟฟ้า สูบน้ำเข้านาให้ดินชุ่มชื้น ระดับน้ำ 3-5 ซม.", USE_WATER_PUMP, 1),
    STORE_ELECTRICITY_TWELVE("ช่วงระยะข้าวออกรวง","ห้ามปล่อยให้นาแห้งในช่วงนี้ เพราะอาจทำให้เมล็ดลีบหรือพัฒนาไม่สมบูรณ์ เฝ้าระวังศัตรูพืช เช่น เพลี้ยกระโดด หนอนกอ และโรคเมล็ดด่าง", 0, 9),
    PUMPING_WATER_TEN("สูบน้ำ (ครั้งที่ 10)","ใช้เครื่องสูบน้ำไฟฟ้า สูบน้ำเข้านาให้ดินชุ่มชื้น ระดับน้ำ 3-5 ซม.", USE_WATER_PUMP, 11),
    PUMPING_WATER_ELEVEN("สูบน้ำ (ครั้งที่ 11)","ใช้เครื่องสูบน้ำไฟฟ้า สูบน้ำเข้านาให้ดินชุ่มชื้น ระดับน้ำ 3-5 ซม.", USE_WATER_PUMP, 1),
    STORE_ELECTRICITY_THIRTEEN("ช่วงระยะข้าวสุกแก่","เมล็ดข้าวเปลี่ยนสีจากเขียวเป็นเหลืองทอง รวงข้าวเอนลงจากน้ำหนักเมล็ดที่เต็มสมบูรณ์ ปลายของรวงข้าวแห้งและกรอบ", 0, 22),
    WAITING_TO_HARVEST("ระบายน้ำออกจากนา","ระบายน้ำออกทั้งหมดในช่วง 5-6 วันก่อนการเก็บเกี่ยว", 0, 6),
    HARVEST("เก็บเกี่ยวข้าว","หลังการเก็บเกี่ยว นำข้าวไปตากแดดเพื่อลดความชื้นเหลือ 13-15% ก่อนการเก็บรักษาหรือส่งขาย", 0, 4);

    private final String name;
    private final String description;
    private final double baseElectricityRequired;
    private final int duration;
    private int currentDuration;

    Rice_Phitsanulok_2_ActivityType(String name, String description, double baseElectricityRequired, int duration) {
        this.name = name;
        this.description = description;
        this.baseElectricityRequired = baseElectricityRequired;
        this.duration = duration;
        this.currentDuration = duration;
    }

    public void setDuration(int duration) {
        this.currentDuration = duration;
    }

    public int getDuration() {
        return currentDuration;
    }

    public int getBaseDuration() {
        return duration;
    }

    public void resetDuration() {
        this.currentDuration = this.duration;
    }

    public double getElectricityRequired(double areaInRai) {
        return this.baseElectricityRequired * areaInRai;
    }
}
