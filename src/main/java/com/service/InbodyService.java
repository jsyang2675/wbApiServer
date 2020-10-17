package com.service;

import com.enums.Gender;
import com.enums.inbody.InbodyBodyType;
import com.enums.inbody.InbodyStandardType;
import com.model.request.InbodyRequest;
import com.model.response.InbodyResponse;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InbodyService {

    private static final Map<String, StandardValues> standardMap = new HashMap();

    private void setStandardValue() {
        standardMap.put(Gender.MAN.name(), new StandardValues(0.15,0.85,
                22,0.475,0.15));
        standardMap.put(Gender.WOMAN.name(), new StandardValues(0.23, 0.77,
                21,0.418,0.186));
    }

    /**
     * 인바디점수, 체형정보, 표준체중률, 표준골격률, 표준체지방률 계산
     * @param requestForm
     * @return
     */
    public InbodyResponse calcInbodyResult(InbodyRequest requestForm) throws Exception {
        //남자,여자별 표준 값 담기
        setStandardValue();
        StandardValues standardValues = standardMap.get(requestForm.getGender());
        /**
         * 인바디 점수 계산
         */
        //1.표준체중 = 키^2 * 표준BMI
        double standardWeight = Math.round(Math.pow(requestForm.getHeight(),2) * standardValues.getStandardBMI()*100)/100.0;
        //2.표준체지방량(FAT) = 표준체중 * 표준체지방률
        double standardFAT = Math.round(standardWeight * standardValues.getStandardBodyFatWeightPercentage()*100)/100.0;
        //3.표준제지방량(FFM) = 표준체중 * 표준제지방률
        double standardFFM = Math.round(standardWeight * standardValues.getStandardFatFreePercentage()*100)/100.0;

        //체지방량 차이 [사용자체지방량 - 표준체지방량]
        double diffBodyFatWeight = requestForm.getBodyFatWeight() - standardFAT;
        //제지방량 차이 [사용자제지방량(체중-체지방량) - 표준제지방량]
        double diffFatFree = requestForm.getWeight() - requestForm.getBodyFatWeight() - standardFFM;

        //인바디점수 = 80 + 제지방량(FFM)차이 - 체지방량(FAT)차이
        int inbodyScore = 80 + (int)(Math.round(diffFatFree) - Math.round(diffBodyFatWeight));

        //위에서 계산 된 표준체중 set
        standardValues.setCalcStandardWeight(standardWeight);

        //표준체중률
        double standardWeightPercentage = calcStandardWeightPercentage(requestForm, standardValues);
        //표준골격률
        double standardPhysiqueWeightPercentage = calcStandardPhysiqueWeightPercentage(requestForm, standardValues);
        //표준체지방률
        double standardBodyFatWeightPercentage = calcStandardBodyFatWeightPercentage(requestForm, standardValues);

        //인바디 체형정보
        InbodyBodyType inbodyBodyType = getInbodyBodyType(standardWeightPercentage,
                standardPhysiqueWeightPercentage, standardBodyFatWeightPercentage);

        return new InbodyResponse(standardWeightPercentage, standardPhysiqueWeightPercentage,
                standardBodyFatWeightPercentage, inbodyScore, inbodyBodyType);
    }

    /**
     * 인바디 체형 정보
     * -표준체중 표준범위 85~115%
     * -표준골격근량 표준범위 90~110%
     * -표준체지방 표준범위 80~160%
     */
    private InbodyBodyType getInbodyBodyType(double standardWeightPercentage,
                                     double standardPhysiqueWeightPercentage, double standardBodyFatWeightPercentage) {
        //체중 범위 지정
        InbodyStandardType weightRange = InbodyStandardType.표준;
        if(standardWeightPercentage < 85) weightRange = InbodyStandardType.표준이하;
        else if(standardWeightPercentage > 115) weightRange = InbodyStandardType.표준이상;
        //골격근량 범위 지정
        InbodyStandardType physiqueWeightRange = InbodyStandardType.표준;
        if(standardPhysiqueWeightPercentage < 90) physiqueWeightRange = InbodyStandardType.표준이하;
        else if(standardPhysiqueWeightPercentage > 110) physiqueWeightRange = InbodyStandardType.표준이상;
        //체지방량 범위 지정
        InbodyStandardType bodyFatWeightRange = InbodyStandardType.표준;
        if(standardBodyFatWeightPercentage < 80) bodyFatWeightRange = InbodyStandardType.표준이하;
        else if(standardBodyFatWeightPercentage > 160) bodyFatWeightRange = InbodyStandardType.표준이상;

        /**
         * 체중 기준으로 표준형,저체중형,과체중형 나눠짐
         */
        if(weightRange == InbodyStandardType.표준)
            //골격근량 표준이상 && 체지방량 표준 또는 표준이하
            if(physiqueWeightRange == InbodyStandardType.표준이상
                    && (bodyFatWeightRange == InbodyStandardType.표준 || bodyFatWeightRange == InbodyStandardType.표준이하))
                return InbodyBodyType.표준체중강인형;
            //골격근량 표준 또는 표준이하 && 체지방량 표준이상
            else if((physiqueWeightRange == InbodyStandardType.표준 || physiqueWeightRange == InbodyStandardType.표준이하)
                    && bodyFatWeightRange == InbodyStandardType.표준이상)
                return InbodyBodyType.표준체중비만형;
            //골격근량 표준이하 && 체지방량 표준
            else if(physiqueWeightRange == InbodyStandardType.표준이하 && bodyFatWeightRange == InbodyStandardType.표준)
                return InbodyBodyType.표준체중허약형;
            else return InbodyBodyType.표준체중표준형;
        else if(weightRange == InbodyStandardType.표준이하)
            //골격근량 표준 && 체지방량 표준이하
            if(physiqueWeightRange == InbodyStandardType.표준 && bodyFatWeightRange == InbodyStandardType.표준이하)
                return InbodyBodyType.저체중강인형;
            //골격근량 표준 && 체지방량 표준이상
            else if(physiqueWeightRange == InbodyStandardType.표준 && bodyFatWeightRange == InbodyStandardType.표준이상)
                return InbodyBodyType.저체중비만형;
            //골격근량 표준이하 && 체지방량 표준이하
            else if(physiqueWeightRange == InbodyStandardType.표준이하 && bodyFatWeightRange == InbodyStandardType.표준이하)
                return InbodyBodyType.저체중허약형;
            else return InbodyBodyType.저체중표준형;
        else
            //골격근량 표준이상 && 체지방량 표준
            if(physiqueWeightRange == InbodyStandardType.표준이상 && bodyFatWeightRange == InbodyStandardType.표준)
                return InbodyBodyType.과체중강인형;
            //골격근량 표준이상 && 체지방량 표준이상
            else if(physiqueWeightRange == InbodyStandardType.표준이상 && bodyFatWeightRange == InbodyStandardType.표준이상)
                return InbodyBodyType.과체중비만형;
            //골격근량 표준 && 체지방량 표준이상
            else if(physiqueWeightRange == InbodyStandardType.표준 && bodyFatWeightRange == InbodyStandardType.표준이상)
                return InbodyBodyType.과체중허약형;
            else return InbodyBodyType.과체중표준형;
    }

    /**
     * 표준 체중 퍼센티지 계산
     * 표준 범위 85~115%
     * 기준 값 : 표준체중
     */
    private Double calcStandardWeightPercentage(InbodyRequest requestForm, StandardValues standardValues) {
        double weightStandardPercentage = requestForm.getWeight() / standardValues.getCalcStandardWeight() * 100;
        return Math.round(weightStandardPercentage*100)/100.0;
    }

    /**
     * 표준 골격근량 퍼센티지 계산
     * 표준 범위 90~110%
     * 기준 값 : 표준체중 * [ 남자 : 45~50% -> 47.5%, 여자 : 37~40% -> 38.5% ]
     *          [ 표준체중 = 키(m)^2 * 표준BMI ]
     */
    private Double calcStandardPhysiqueWeightPercentage(InbodyRequest requestForm, StandardValues standardValues) {
        //사용자 골격근량
        double userPhysiqueWeight = requestForm.getPhysiqueWeight();
        //기준 값 계산
        double physiqueWeightBase = standardValues.getCalcStandardWeight() * standardValues.getPhysiqueWeightBasePercentage();
        //표준골격근량 퍼센티지 계산
        double physiqueWeightStandardPercentage = userPhysiqueWeight / physiqueWeightBase * 100;
        return Math.round(physiqueWeightStandardPercentage*100)/100.0;
    }

    /**
     * 표준 체지방량 퍼센티지 계산
     * 표준 범위 80~160%
     * 기준 값 : 표준체중 * [ 남자 : 15%, 여자 : 23% ]
     */
    private Double calcStandardBodyFatWeightPercentage(InbodyRequest requestForm, StandardValues standardValues) {
        //사용자 체지방량
        double userBodyFatWeight = requestForm.getBodyFatWeight();
        //기준 값 계산
        double bodyFatWeightBase = standardValues.getCalcStandardWeight() * standardValues.getBodyFatWeightBasePercentage();

        //표준골격근량 퍼센티지 계산
        double bodyFatWeightStandardPercentage = userBodyFatWeight / bodyFatWeightBase * 100;
        return Math.round(bodyFatWeightStandardPercentage*100)/100.0;
    }

    @Data
    public static class StandardValues {
        private double standardBodyFatWeightPercentage;   //표준체지방퍼센티지
        private double standardFatFreePercentage;         //표준제지방퍼센티지
        private int standardBMI;                          //표준BMI
        private double physiqueWeightBasePercentage;      //골격근량기준퍼센티지
        private double bodyFatWeightBasePercentage;       //체지방량기준퍼센티지
        private double calcStandardWeight;                //계산된표준체중

        public StandardValues(double standardBodyFatWeightPercentage, double standardFatFreePercentage, int standardBMI,
                             double physiqueWeightBasePercentage, double bodyFatWeightBasePercentage) {
            this.standardBodyFatWeightPercentage = standardBodyFatWeightPercentage;
            this.standardFatFreePercentage = standardFatFreePercentage;
            this.standardBMI = standardBMI;
            this.physiqueWeightBasePercentage = physiqueWeightBasePercentage;
            this.bodyFatWeightBasePercentage = bodyFatWeightBasePercentage;
        }
    }

}
