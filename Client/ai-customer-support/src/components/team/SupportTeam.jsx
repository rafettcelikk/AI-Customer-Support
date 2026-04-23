import React from "react";
import Slider from "react-slick";
import { supportTeam } from "../team/supportTeamData.js";

const Slick = Slider.default ? Slider.default : Slider;

const SupportTeam = () => {
  const settings = {
    dots: true,
    infinite: true,
    speed: 10000,
    autoplay: true,
    autoplaySpeed: 3000,
    slidesToShow: 3,
  };
  return (
    <section className="team-section mb-5" id="support">
      <h2 className="team-title">Destek Ekibimiz</h2>
      <Slick {...settings}>
        {supportTeam.map((member) => (
          <div key={member.id} className="team-member">
            <img src={member.image} alt={member.name} />
            <div>
              <span className="member-name">{member.name}</span>
              <br />
              <span className="member-role">{member.role}</span>
            </div>
          </div>
        ))}
      </Slick>
    </section>
  );
};

export default SupportTeam;
